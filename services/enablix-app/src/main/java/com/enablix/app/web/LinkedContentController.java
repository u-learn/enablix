package com.enablix.app.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.search.es.ESQueryBuilder;
import com.enablix.analytics.search.es.ElasticSearchClient;
import com.enablix.analytics.search.es.OrMatchQueryBuilder;
import com.enablix.analytics.search.es.SearchFieldBuilder;
import com.enablix.analytics.search.es.SearchFieldFilter;
import com.enablix.analytics.search.es.SearchHitTransformer;
import com.enablix.analytics.search.es.TypeFilter;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.connection.repo.ContentTypeConnectionRepository;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.core.domain.content.connection.ContentValueConnection;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;
import com.enablix.services.util.TemplateUtil;

@RestController
@RequestMapping("content")
public class LinkedContentController {

	private static final String ATTR_ID_OR_SEP = "|";
	private static final String SPLIT_ATTR_ID_REGEX = "\\|";
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DisplayableContentBuilder contentBuilder;
	
	@Autowired
	private ContentCrudService contentCrud;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Autowired
	private TextLinkProcessor textLinkProcessor;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Autowired
	private ElasticSearchClient esClient;
	
	@Autowired
	private SearchHitTransformer searchHitTx;
	
	@Autowired
	private ContentTypeConnectionRepository contentTypeConnRepo;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/le/{contentQId}/{attrId}/{attrVal}/lt/{lookupContentQId}/cat/{businessCategory}/", 
			produces = "application/json")
	public List<LinkedContent> getSpecificLinkedContent(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal, @PathVariable String lookupContentQId,
			@PathVariable String businessCategory,
			@RequestHeader(value="requestorId", required=false) String userEmailId) {
		return fetchLinkedContent(contentQId, attrId, attrVal, lookupContentQId, businessCategory, userEmailId);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/le/{contentQId}/{attrId}/{attrVal}/cat/{businessCategory}/", 
			produces = "application/json")
	public List<LinkedContent> getLinkedContent(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal, @PathVariable String businessCategory,
			@RequestHeader(value="requestorId", required=false) String userEmailId) {
		
		return fetchLinkedContent(contentQId, attrId, attrVal, null, businessCategory, userEmailId);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/le/{contentQId}/{attrId}/{attrVal}/mc/{mContentQId}/{mAttrId}/{mAttrVal}/cat/{businessCategory}/", 
			produces = "application/json")
	public List<DisplayableContent> getMappedContent(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal, @PathVariable String mContentQId, 
			@PathVariable String mAttrId, @PathVariable String mAttrVal,
			@PathVariable String businessCategory,
			@RequestHeader(value="requestorId", required=false) String userEmailId) {
		
		ContainerBusinessCategoryType bizCategory = getBusinessCategoryEnumValue(businessCategory);
		
		List<DisplayableContent> mappedContent = null;
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		ContainerType containerDef = template.getContainerDefinition(contentQId);
		
		if (containerDef != null) {
		
			DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
			MongoDataView mdbView = DataViewUtil.getMongoDataView(userDataView);
			
			List<Map<String, Object>> cRecords = findContentRecord(contentQId, attrId, attrVal, template, mdbView);
			
			if (CollectionUtil.isNotEmpty(cRecords)) {
				
				List<String> cRecordIdentities =  CollectionUtil.transform(cRecords, 
						() -> new ArrayList<String>(), (refRec) -> ContentDataUtil.getRecordIdentity(refRec));
				
				List<String> restrictedContentTypes = findMappedContentTypes(
						mContentQId, mAttrId, mAttrVal, template, mdbView);
				
				LookupConfig lcLookupConfig = createLinkedContainerLookupConfig(template, containerDef, bizCategory);
				
				LookupConfig containerLC = new LookupConfig();
				
				if (restrictedContentTypes != null) {
					
					for (String restrictedContQId : restrictedContentTypes) {
				
						String restCollName = template.getCollectionName(restrictedContQId);
						containerLC.addLookupConfig(restrictedContQId, restCollName, 
							lcLookupConfig.containerQIdToFieldId.get(restrictedContQId));
					}
					
					mappedContent = findLinkedContent(template, cRecordIdentities, containerLC, userEmailId);
				}
				
			}
		}
		
		return mappedContent == null ? new ArrayList<>() : mappedContent;
	}

	private List<String> findMappedContentTypes(String mContentQId, String mAttrId, String mAttrVal,
			TemplateFacade template, MongoDataView mdbView) {
		
		List<ContentTypeConnection> contentMapping = contentTypeConnRepo.findByContentQId(mContentQId);
		List<String> restrictedContentTypes = null;
		
		if (CollectionUtil.isNotEmpty(contentMapping)) {
		
			// get the first content mapping
			ContentTypeConnection contentTypeConn = contentMapping.get(0);
			
			List<Map<String, Object>> mRecords = findContentRecord(mContentQId, mAttrId, mAttrVal, template, mdbView);
			if (CollectionUtil.isNotEmpty(mRecords)) {
				
				Map<String, Object> mRecord = mRecords.get(0);
				String mRecIdentity = ContentDataUtil.getRecordIdentity(mRecord);
				
				for (ContentValueConnection valConn : contentTypeConn.getConnections()) {
					if (valConn.getContentValue().equals(mRecIdentity)) {
						restrictedContentTypes = valConn.getConnectedContainers();
						break;
					}
				}
			}
			
		}
		return restrictedContentTypes;
	}
		
	private List<LinkedContent> fetchLinkedContent(String contentQId, String attrId, 
			String attrVal, String lookupContentQId, String businessCategory, String userEmailId) {
		
		List<LinkedContent> linkedContents = new ArrayList<>();
		
		ContainerBusinessCategoryType bizCategory = getBusinessCategoryEnumValue(businessCategory);
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		ContainerType containerDef = template.getContainerDefinition(contentQId);
		
		if (containerDef != null) {
		
			DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
			MongoDataView mdbView = DataViewUtil.getMongoDataView(userDataView);
			
			List<Map<String, Object>> refRecords = findContentRecord(contentQId, attrId, attrVal, template, mdbView);
			
			if (CollectionUtil.isNotEmpty(refRecords)) {
			
				List<String> instanceIdentities =  CollectionUtil.transform(refRecords, 
						() -> new ArrayList<String>(), (refRec) -> ContentDataUtil.getRecordIdentity(refRec));
				
				LookupConfig lookupConfig = createLinkedContainerLookupConfig(template, containerDef, bizCategory);
				
				Map<String, Long> collRecordCnt = findLinkedContentCount(template, instanceIdentities, lookupConfig);
				
				String lookupColl = null;
				
				if (StringUtil.hasText(lookupContentQId)) {
					lookupColl = template.getCollectionName(lookupContentQId);
				}

				
				LinkedContent selectedLinkedContent = null;
				
				for (ContainerType linkedCont: containerDef.getContainer()) {
					
					if (TemplateUtil.isLinkedContainer(linkedCont)) {
					
						String linkContQId = linkedCont.getLinkContainerQId();
						String linkCollName = template.getCollectionName(linkContQId);
						
						Long recCnt = collRecordCnt.get(linkCollName);
						if (recCnt != null) {
						
							LinkedContent linkContent = new LinkedContent();
							
							linkContent.setContentQId(linkContQId);
							linkContent.setContentLabel(linkedCont.getLabel());
							linkContent.setContentCount(recCnt.intValue());
							
							if (linkContQId.equals(lookupContentQId)) {
								linkContent.setSelected(true);
								selectedLinkedContent = linkContent;
							}
							
							linkedContents.add(linkContent);
						}
					}
				}
				
				Collections.sort(linkedContents);
				
				if (!StringUtil.hasText(lookupContentQId)) {
					
					if (!linkedContents.isEmpty()) {
						
						selectedLinkedContent = linkedContents.get(0);
						selectedLinkedContent.setSelected(true);
						
						lookupColl = template.getCollectionName(selectedLinkedContent.getContentQId());
						lookupContentQId = selectedLinkedContent.getContentQId();
					}
				}
				
				if (lookupColl != null && selectedLinkedContent != null) {
					
					LookupConfig containerLC = new LookupConfig();
					containerLC.addLookupConfig(lookupContentQId, lookupColl, 
							lookupConfig.containerQIdToFieldId.get(lookupContentQId));
					
					List<DisplayableContent> displayRecords = 
							findLinkedContent(template, instanceIdentities, containerLC, userEmailId);
					
					if (selectedLinkedContent != null) {
						selectedLinkedContent.setRecords(displayRecords);
					}
					
				}
				
			}
		}
		
		return linkedContents;
	}

	private ContainerBusinessCategoryType getBusinessCategoryEnumValue(String businessCategory) {
		
		ContainerBusinessCategoryType bizCategory = null;
		
		try {
			bizCategory = ContainerBusinessCategoryType.fromValue(businessCategory);
		} catch (Throwable t) {
			// ignore invalid value
		}
		
		return bizCategory;
	}

	private LookupConfig createLinkedContainerLookupConfig(TemplateFacade template, 
			ContainerType containerDef, ContainerBusinessCategoryType businessCategory) {
		
		// find the linked containers for this content type
		LookupConfig lookupConfig = new LookupConfig();
		
		if (containerDef.getContainer() != null) {
			
			containerDef.getContainer().forEach((linkedContainer) -> {
			
				if (TemplateUtil.isLinkedContainer(linkedContainer)) {
					
					String linkContainerQId = linkedContainer.getLinkContainerQId();
					String linkedCollName = template.getCollectionName(linkContainerQId);
					
					ContainerType linkContDef = template.getContainerDefinition(linkContainerQId);
					
					if (businessCategory == null || linkContDef.getBusinessCategory() == businessCategory) {
						lookupConfig.addLookupConfig(linkContainerQId, linkedCollName, linkedContainer.getLinkContentItemId());
					}
				}
				
			});
		}
		return lookupConfig;
	}

	private List<Map<String, Object>> findContentRecord(String contentQId, String attrId, String attrVal,
			TemplateFacade template, MongoDataView mdbView) {
		
		SearchFilter filter = null;
		
		if (attrId.contains(ATTR_ID_OR_SEP)) {
		
			String[] attrs = attrId.split(SPLIT_ATTR_ID_REGEX);
			
			for (String attr : attrs) {
				
				StringFilter attrFilter = new StringFilter(attr, attrVal, ConditionOperator.EQ);
				
				if (filter == null) {
					filter = attrFilter;
				} else {
					filter = filter.or(attrFilter);
				}
			}
			
		} else {
			filter = new StringFilter(attrId, attrVal, ConditionOperator.EQ);
		}
		
		String collectionName = template.getCollectionName(contentQId);
		
		// find the content record matching the attribute input
		return contentCrud.findRecords(collectionName, filter, mdbView);
	}

	private List<DisplayableContent> findLinkedContent(TemplateFacade template, List<String> instanceIdentities,
			LookupConfig containerLC, String userEmailId) {
		
		OrMatchQueryBuilder orMatch = new OrMatchQueryBuilder(instanceIdentities);
		
		SearchRequest recSearchRequest = ESQueryBuilder.builder(orMatch, template, containerLC)
				.withPagination(20, 0)
				.withFuzziness((searchTerm) -> null)
				.withTypeFilter(containerLC)
				.withFieldFilter(containerLC)
				.withOptimizer((mmQueryBuilder) -> mmQueryBuilder.minimumShouldMatch("100%"))
				.build();
		
		SearchHits searchResult = esClient.searchContent(recSearchRequest);
		
		List<ContentDataRecord> linkedContent = new ArrayList<>();
		for (SearchHit hit : searchResult) {
			ContentDataRecord linkedRecord = searchHitTx.toContentDataRecord(hit, template);
			linkedContent.add(linkedRecord);
		}
		
		List<DisplayableContent> displayRecords = new ArrayList<>();
		DisplayContext ctx = new DisplayContext();
		
		for (ContentDataRecord record : linkedContent) {
			
			DisplayableContent dispRecord = contentBuilder.build(template, record, ctx);
			
			docUrlPopulator.populateUnsecureUrl(dispRecord, userEmailId);
			textLinkProcessor.process(dispRecord, template, userEmailId);
			displayRecords.add(dispRecord);
		}
		
		return displayRecords;
	}

	private Map<String, Long> findLinkedContentCount(TemplateFacade template, 
			List<String> referenceRecIdentity, LookupConfig lookupConfig) {
		
		TermsBuilder recordCountAgg = AggregationBuilders.terms("record_count");
		recordCountAgg.field("_type");
		recordCountAgg.size(50);
		
		OrMatchQueryBuilder orMatch = new OrMatchQueryBuilder(referenceRecIdentity);
		
		SearchRequest searchRequest = ESQueryBuilder.builder(orMatch, template, lookupConfig)
				.withPagination(0, 0) // set 0 for aggregation
				.withFuzziness((searchTerm) -> null)
				.withTypeFilter(lookupConfig)
				.withFieldFilter(lookupConfig)
				.withOptimizer((mmQueryBuilder) -> mmQueryBuilder.minimumShouldMatch("100%"))
				.withAggregation(recordCountAgg)
				.build();
		
		ActionFuture<SearchResponse> result = esClient.executeSearch(searchRequest);
		SearchResponse res = result.actionGet();
		Aggregations aggregations = res.getAggregations();
		Aggregation aggResult = aggregations.get("record_count");
		
		Map<String, Long> collRecordCnt = new HashMap<>();
		
		if (aggResult instanceof StringTerms) {
			StringTerms termsAggResult = (StringTerms) aggResult;
			List<Bucket> buckets = termsAggResult.getBuckets();
			for (Bucket bucket : buckets) {
				String collName = bucket.getKeyAsString();
				long recordCnt = bucket.getDocCount();
				collRecordCnt.put(collName, recordCnt);
			}
		}
		
		return collRecordCnt;
	}
	
	
	
	public static class LinkedContent implements Comparable<LinkedContent> {
		
		private String contentQId;
		private String contentLabel;
		private boolean selected;
		private int contentCount;
		private List<DisplayableContent> records;
		
		public String getContentQId() {
			return contentQId;
		}
		
		public void setContentQId(String contentQId) {
			this.contentQId = contentQId;
		}
		
		public String getContentLabel() {
			return contentLabel;
		}
		
		public void setContentLabel(String contentLabel) {
			this.contentLabel = contentLabel;
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public int getContentCount() {
			return contentCount;
		}

		public void setContentCount(int contentCount) {
			this.contentCount = contentCount;
		}

		public List<DisplayableContent> getRecords() {
			return records;
		}
		
		public void setRecords(List<DisplayableContent> records) {
			this.records = records;
		}
		
		@Override
		public int compareTo(LinkedContent o) {
			return contentLabel.compareTo(o.contentLabel);
		}
		
	}
	
	private static class LookupConfig implements TypeFilter, SearchFieldFilter, SearchFieldBuilder {
		
		private Map<String, String> containerQIdToFieldId = new HashMap<>();
		private Set<String> collectionNames = new HashSet<>();
		

		void addLookupConfig(String containerQId, String collectionName, String fieldId) {
			containerQIdToFieldId.put(containerQId, fieldId);
			collectionNames.add(collectionName);
		}
		
		@Override
		public boolean searchIn(String containerQId, ContentItemType field) {
			String containerField = containerQIdToFieldId.get(containerQId);
			return field.getId().equals(containerField);
		}

		@Override
		public boolean searchIn(String fieldId) {
			return containerQIdToFieldId.values().contains(fieldId);
		}

		@Override
		public boolean searchInType(String esType) {
			return collectionNames.contains(esType);
		}

		@Override
		public String[] getContentSearchFields(SearchFieldFilter filter, TemplateFacade template) {
			Set<String> searchFields = new HashSet<>();
			containerQIdToFieldId.values().forEach((fieldId) -> searchFields.add(fieldId + ".id"));
			return searchFields.toArray(new String[0]);
		}
	}
	
}
