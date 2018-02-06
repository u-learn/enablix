package com.enablix.bayes;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.bayes.finding.NodeFindingProviderFactory;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.util.MultiTenantExecutor;
import com.enablix.core.mongo.util.MultiTenantExecutor.TenantTask;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.services.util.ContentDataUtil;

import norsys.netica.NeticaException;

@Component
public class BayesServiceImpl implements BayesService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(BayesServiceImpl.class);

	@Value("${bayes.output.dir}")
	private String outputDir;
	
	@Autowired
	private NodeFindingProviderFactory providerFactory;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentCrudService contentCrud;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Override
	public void runBatch(ExecutionContext ctx, EBXNet net) throws Exception {
		
		// Initialize finding providers
		for (EBXNode node : net.getNodes()) {
			
			NodeFindingProvider findingProvider = providerFactory.getFindingProvider(node.getName());
			
			if (findingProvider != null) {
				
				LOGGER.debug("Initializing node: {}", node.getName());
				
				findingProvider.init(ctx);
				
			} else {
				LOGGER.info("Node Finding Provider not found for node [{}]", node.getName());
			}
		}
		
		net.compile();
		
		LOGGER.debug("{}", net);
		
		MultiTenantExecutor.executeForEachTenant(ctx.getExecuteForTenants(), 
					new RelevanceCalculator(net, ctx));
		
	}
	
	private FlatFileItemWriter<RelevanceInfo> createItemWriter(ExecutionContext ctx) {
		
		FlatFileItemWriter<RelevanceInfo> csvWriter = new FlatFileItemWriter<>();
		
		DelimitedLineAggregator<RelevanceInfo> lineAggregator = new DelimitedLineAggregator<>();
		RelevanceInfoFieldExtractor fieldExtractor = new RelevanceInfoFieldExtractor();
		
		lineAggregator.setFieldExtractor(fieldExtractor);
		csvWriter.setLineAggregator(lineAggregator);
		
		String outFile = outputDir + File.separator + "content-bayes-" 
				+ ProcessContext.get().getTenantId() + "-"
				+ DateUtil.dateToLongDateString(ctx.getRunAsDate()) + "-" 
				+ Calendar.getInstance().getTimeInMillis() + ".csv";
		
		csvWriter.setResource(new FileSystemResource(outFile));
		
		csvWriter.setHeaderCallback(fieldExtractor);
		
		return csvWriter;
	}

	public class RelevanceCalculator implements TenantTask {

		private EBXNet net;
		private ExecutionContext ctx;
		
		public RelevanceCalculator(EBXNet net, ExecutionContext ctx) {
			super();
			this.net = net;
			this.ctx = ctx;
		}

		@Override
		public void execute() {
			
			FlatFileItemWriter<RelevanceInfo> itemWriter = createItemWriter(ctx);
			
			try {
				
				itemWriter.open(new org.springframework.batch.item.ExecutionContext());
				
				String templateId = ProcessContext.get().getTemplateId();
				TemplateFacade template = templateMgr.getTemplateFacade(templateId);
				
				for (ContainerType container : template.getBizContentContainers()) {
					
					String contentQId = container.getQualifiedId();
					String collectionName = template.getCollectionName(contentQId);
					
					int pageno = 0;
					
					Page<Map<String, Object>> dataPage = null;
					
					do {
						
						Pageable pageable = new PageRequest(pageno, 10);
						dataPage = contentCrud.findAllRecord(collectionName, pageable, MongoDataView.ALL_DATA);
	
						for (Map<String, Object> rec : dataPage) {
							
							ContentDataRecord dataRec = new ContentDataRecord(templateId, contentQId, rec);
							String recIdentity = ContentDataUtil.getRecordIdentity(rec);
							String title = (String) rec.get(ContentDataConstants.CONTENT_TITLE_KEY);
							
							LOGGER.debug("Running bayes net for: contentQId - {}, identity - {}, title - {}", contentQId, recIdentity, title);
							
							List<RelevanceInfo> recRelevance = new ArrayList<>();
							
							int userPageNo = 0;
							Page<UserProfile> userPage = null;
							
							
							do {
								
								Pageable userPageable = new PageRequest(userPageNo, 10);
								
								if (CollectionUtil.isEmpty(ctx.getExecuteForUsers())) {
									userPage = userProfileRepo.findAll(userPageable);
								} else {
									userPage = userProfileRepo.findByEmailIn(ctx.getExecuteForUsers(), userPageable);
								}
								
								for (UserProfile user : userPage) {
									
									try {
										
										net.getNet().retractFindings();
										
									} catch (NeticaException e1) {
										throw new RuntimeException("Unable to retract findings", e1);
									}
									
									// calculate and set finding for this user and data record
									LOGGER.debug("Setting findings for user - {}", user.getEmail());
									
									for (EBXNode node : net.getNodes()) {
										
										NodeFindingProvider findingProvider = providerFactory.getFindingProvider(node.getName());
										
										if (findingProvider != null) {
	
											String finding = findingProvider.getFinding(ctx, user, dataRec);
											
											if (StringUtil.hasText(finding)) {
												
												LOGGER.debug("Finding {} - {}", node.getName(), finding);
											
												try {
													node.getNode().finding().enterState(finding);
												} catch (NeticaException e) {
													throw new RuntimeException("Unable to set finding [" + finding 
															+ "] for node [" + node.getName() + "]", e);
												}
											}
	
										}
									}
									
									// not find out the relevance score
									float relevance = net.getProbability();
									
									// LOGGER.debug("Bayes net: {}", net);
									
									RelevanceInfo info = new RelevanceInfo(user.getEmail(), contentQId, 
											container.getLabel(), recIdentity, title, relevance);
									
									for (EBXNode node : net.getNodes()) {
										info.nodeRelevance.put(node.getName(), node.getBelief(EBXNet.TRUE));
									}
									
									recRelevance.add(info);
									
									LOGGER.debug("{}", info);
									
								}
								
								userPageNo++;
								
							} while (userPage != null && userPage.hasNext());
							
							// write the results
							try {
								itemWriter.write(recRelevance);
							} catch (Exception e) {
								LOGGER.error("Error writing relevance results", e);
								throw new RuntimeException(e);
							}
							
						}
						
						pageno++;
						
					} while (dataPage != null && dataPage.hasNext());
				}
				
			} finally {
				itemWriter.close();
			}
			
		}
		
	}
	
	public static final class RelevanceInfo {
		
		private String userId;
		
		private String contentQId;
		
		private String containerLabel;
		
		private String contentIdentity;
		
		private String contentTitle;
		
		private float relevance;
		
		private Map<String, Float> nodeRelevance;

		public RelevanceInfo(String userId, String contentQId, String containerLabel, 
				String contentIdentity, String contentTitle, float relevance) {
			super();
			this.userId = userId;
			this.contentQId = contentQId;
			this.containerLabel = containerLabel;
			this.contentIdentity = contentIdentity;
			this.contentTitle = contentTitle;
			this.relevance = relevance;
			this.nodeRelevance = new HashMap<>();
		}
		
		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getContentQId() {
			return contentQId;
		}

		public void setContentQId(String contentQId) {
			this.contentQId = contentQId;
		}
		
		public String getContainerLabel() {
			return containerLabel;
		}

		public void setContainerLabel(String containerLabel) {
			this.containerLabel = containerLabel;
		}

		public String getContentIdentity() {
			return contentIdentity;
		}

		public void setContentIdentity(String contentIdentity) {
			this.contentIdentity = contentIdentity;
		}

		public String getContentTitle() {
			return contentTitle;
		}

		public void setContentTitle(String contentTitle) {
			this.contentTitle = contentTitle;
		}

		public float getRelevance() {
			return relevance;
		}

		public void setRelevance(float relevance) {
			this.relevance = relevance;
		}

		@Override
		public String toString() {
			return "RelevanceInfo [userId=" + userId + ", contentQId=" + contentQId + ", containerLabel="
					+ containerLabel + ", contentIdentity=" + contentIdentity + ", contentTitle=" + contentTitle
					+ ", relevance=" + relevance + "]";
		}

	}
	
	public static class RelevanceInfoFieldExtractor implements FieldExtractor<RelevanceInfo>, FlatFileHeaderCallback {

		private static final String[] NODE_LIST = { 
				ContentBayesNet.IS_NEW_CONTENT_NN, 
				ContentBayesNet.IS_RECENTLY_UPDT_CONTENT_NN,
				ContentBayesNet.IS_CONTENT_UPDT_AFTER_ACCESS_NN,
				ContentBayesNet.IS_RECENT_CONTENT_NN,
				ContentBayesNet.PEER_ACCESSED_CONTENT_NN,
				ContentBayesNet.PEER_ACCESSED_CONTENT_TYPE_NN,
				ContentBayesNet.IS_POPULAR_AMONG_PEERS_NN,
		};
		

		@Override
		public void writeHeader(Writer writer) throws IOException {
			writer.write("User Id,Content Type,Content Identity,Content Title, Relevance," 
							+ StringUtils.arrayToCommaDelimitedString(NODE_LIST));
		}

		@Override
		public Object[] extract(RelevanceInfo item) {
			
			List<Object> propValues = new ArrayList<>(5 + NODE_LIST.length);
			
			propValues.add(item.getUserId());
			propValues.add(item.getContainerLabel());
			propValues.add(item.getContentIdentity());
			propValues.add(item.getContentTitle());
			propValues.add(item.getRelevance());
			
			for (String nodeName : NODE_LIST) {
				propValues.add(item.nodeRelevance.get(nodeName));
			}
			
			return propValues.toArray();
		}
		
	}
	
}
