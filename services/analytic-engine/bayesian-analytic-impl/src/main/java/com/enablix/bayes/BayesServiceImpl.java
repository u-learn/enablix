package com.enablix.bayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.bayes.finding.NodeFindingProviderFactory;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.content.UserContentRelevance;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.util.MultiTenantExecutor;
import com.enablix.core.mongo.util.MultiTenantExecutor.TenantTask;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;

import norsys.netica.NeticaException;

@Component
public class BayesServiceImpl implements BayesService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(BayesServiceImpl.class);

	@Autowired
	private NodeFindingProviderFactory providerFactory;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentCrudService contentCrud;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Override
	public void runBatch(ExecutionContext ctx, EBXNet net, RelevanceRecorder recorder) throws Exception {
		
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
					new RelevanceCalculator(net, ctx, recorder));
		
	}
	
	public class RelevanceCalculator implements TenantTask {

		private EBXNet net;
		private ExecutionContext ctx;
		private RelevanceRecorder recorder;
		
		public RelevanceCalculator(EBXNet net, ExecutionContext ctx, RelevanceRecorder recorder) {
			super();
			this.net = net;
			this.ctx = ctx;
			this.recorder = recorder;
		}

		@Override
		public void execute() {
			
			try {
				
				recorder.open(ctx);
				
				String tenantId = ProcessContext.get().getTenantId();
				String templateId = ProcessContext.get().getTemplateId();
				
				TemplateFacade template = templateMgr.getTemplateFacade(templateId);
				
				if (template == null) {
					LOGGER.error("No template [{}] found for tenant [{}]", templateId, tenantId);
					return;
				}
				
				List<ContainerType> bizContentContainers = template.getBizContentContainers();
				if (CollectionUtil.isEmpty(bizContentContainers)) {
					LOGGER.error("No business content containers found for template [{}]", templateId);
					return;
				}
				
				for (ContainerType container : bizContentContainers) {
					
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
							
							List<UserContentRelevance> recRelevance = new ArrayList<>();
							
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
									
									DataView dataView = dataSegmentService.createDataViewForUser(user);
									MongoDataView mongoDataView = DataViewUtil.getMongoDataView(dataView);
									if (mongoDataView != null && !mongoDataView.isRecordVisible(collectionName, rec)) {
										// ignore as user does not have access to this record
										continue;
									}
									
									UserContentRelevance info = calculateRelevance(container, contentQId, dataRec,
											recIdentity, title, user);
									
									recRelevance.add(info);
									
									LOGGER.debug("{}", info);
									
								}
								
								userPageNo++;
								
							} while (userPage != null && userPage.hasNext());
							
							// write the results
							try {
								recorder.write(recRelevance, dataRec);
							} catch (Exception e) {
								LOGGER.error("Error writing relevance results", e);
								throw new RuntimeException(e);
							}
							
						}
						
						pageno++;
						
					} while (dataPage != null && dataPage.hasNext());
				}
				
			} finally {
				recorder.close();
			}
			
		}

		private UserContentRelevance calculateRelevance(ContainerType container, String contentQId,
				ContentDataRecord dataRec, String recIdentity, String title, UserProfile user) {
			
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
			
			UserContentRelevance info = new UserContentRelevance(user.getEmail(), contentQId, 
					container.getLabel(), recIdentity, title, container.getBusinessCategory(), 
					net.getProbability(), ctx.getRunAsDate());
			
			for (EBXNode node : net.getNodes()) {
				for (String state : node.getStates()) {
					info.addNodeRelevance(node.getName(), state, node.getBelief(state));
				}
			}
			
			return info;
		}
		
	}
	
}
