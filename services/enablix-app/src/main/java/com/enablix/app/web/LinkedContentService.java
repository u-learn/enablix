package com.enablix.app.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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
import org.springframework.stereotype.Component;

import com.enablix.analytics.search.es.ESQueryBuilder;
import com.enablix.analytics.search.es.ElasticSearchClient;
import com.enablix.analytics.search.es.OrMatchQueryBuilder;
import com.enablix.analytics.search.es.SearchFieldBuilder;
import com.enablix.analytics.search.es.SearchFieldFilter;
import com.enablix.analytics.search.es.SearchHitTransformer;
import com.enablix.analytics.search.es.TypeFilter;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.connection.repo.ContentTypeConnectionRepository;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.LinkedContent;
import com.enablix.core.api.LinkedContentRequest;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.core.domain.content.connection.ContentValueConnection;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class LinkedContentService {

	private static final String ATTR_ID_OR_SEP = "|";
	private static final String SPLIT_ATTR_ID_REGEX = "\\|";

	@Autowired
	private ElasticSearchClient esClient;
	
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
	private SearchHitTransformer searchHitTx;
	
	@Autowired
	private ContentTypeConnectionRepository contentTypeConnRepo;
	
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
	
	private List<ContentDataRecord> findLinkedContent(TemplateFacade template, 
			List<String> instanceIdentities, LookupConfig containerLC) {
		
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
		
		return linkedContent;
	}

	private List<DisplayableContent> convertToDisplayableContent(TemplateFacade template,
			List<ContentDataRecord> linkedContent, String userEmailId) {
		
		List<DisplayableContent> displayRecords = new ArrayList<>();
		DisplayContext ctx = new DisplayContext();
		
		for (ContentDataRecord record : linkedContent) {
			
			DisplayableContent dispRecord = contentBuilder.build(template, record, ctx);
			
			docUrlPopulator.populateUnsecureUrl(dispRecord, userEmailId, ctx);
			textLinkProcessor.process(dispRecord, template, userEmailId, ctx);
			displayRecords.add(dispRecord);
		}
		
		return displayRecords;
	}
	
	private <T> List<LinkedContent<T>> fetchLinkedContent(String contentQId, String attrId, 
			String attrVal, String lookupContentQId, String businessCategory, 
			Function<List<ContentDataRecord>, List<T>> transformer) {
		
		List<LinkedContent<T>> linkedContents = new ArrayList<>();
		
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

				
				LinkedContent<T> selectedLinkedContent = null;
				
				for (ContainerType linkedCont: containerDef.getContainer()) {
					
					if (TemplateUtil.isLinkedContainer(linkedCont)) {
					
						String linkContQId = linkedCont.getLinkContainerQId();
						String linkCollName = template.getCollectionName(linkContQId);
						
						Long recCnt = collRecordCnt.get(linkCollName);
						if (recCnt != null) {
						
							LinkedContent<T> linkContent = new LinkedContent<>();
							
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
					
					List<ContentDataRecord> displayRecords = 
							findLinkedContent(template, instanceIdentities, containerLC);
					
					if (selectedLinkedContent != null) {
						selectedLinkedContent.setRecords(transformer.apply(displayRecords));
					}
					
				}
				
			}
		}
		
		return linkedContents;
	}
	
	public List<LinkedContent<ContentDataRecord>> fetchLinkedContentData(String contentQId, String attrId, 
			String attrVal, String lookupContentQId, String businessCategory) {
		
		return fetchLinkedContent(contentQId, attrId, attrVal, lookupContentQId, businessCategory, (dataRecords) -> dataRecords);
	}
	
	public List<LinkedContent<DisplayableContent>> fetchDisplayableLinkedContent(String contentQId, String attrId, 
			String attrVal, String lookupContentQId, String businessCategory, String requesterEmailId) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		return fetchLinkedContent(contentQId, attrId, attrVal, lookupContentQId, businessCategory, 
				(dataRecords) -> convertToDisplayableContent(template, dataRecords, requesterEmailId));
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
	
	public List<ContentDataRecord> getLinkedAndMappedContentData(LinkedContentRequest contentRequest) {
		
		String contentQId = contentRequest.getRefContentQId();
		String attrId = contentRequest.getRefMatchAttrId(); 
		String attrVal = contentRequest.getRefMatchAttrValue();
		String mContentQId = contentRequest.getMapContentQId(); 
		String mAttrId = contentRequest.getMapMatchAttrId();
		String mAttrVal = contentRequest.getMapMatchAttrValue();
		String businessCategory = contentRequest.getContentBusinessCategory();
		
		ContainerBusinessCategoryType bizCategory = getBusinessCategoryEnumValue(businessCategory);
		
		List<ContentDataRecord> mappedContent = null;
		
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
				
				if (CollectionUtil.isNotEmpty(restrictedContentTypes)) {
					
					for (String restrictedContQId : restrictedContentTypes) {
				
						String restCollName = template.getCollectionName(restrictedContQId);
						containerLC.addLookupConfig(restrictedContQId, restCollName, 
							lcLookupConfig.containerQIdToFieldId.get(restrictedContQId));
					}
					
					mappedContent = findLinkedContent(template, cRecordIdentities, containerLC);
					
				}
				
			}
		}
		
		return mappedContent == null ? new ArrayList<>() : mappedContent;
	}
	
	public List<DisplayableContent> getLinkedAndMappedDisplayableContent(
			LinkedContentRequest contentRequest, String requesterEmailId) {

		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		List<ContentDataRecord> linkedDataRecords = getLinkedAndMappedContentData(contentRequest);
		
		return convertToDisplayableContent(template, linkedDataRecords, requesterEmailId);
	}
	
	private ContainerBusinessCategoryType getBusinessCategoryEnumValue(String businessCategory) {
		
		ContainerBusinessCategoryType bizCategory = null;
		
		if (businessCategory != null) {
			
			try {
				bizCategory = ContainerBusinessCategoryType.fromValue(businessCategory);
			} catch (Throwable t) {
				// ignore invalid value
			}
		}
		
		return bizCategory;
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
