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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.search.es.ESQueryBuilder;
import com.enablix.analytics.search.es.ElasticSearchClient;
import com.enablix.analytics.search.es.SearchFieldBuilder;
import com.enablix.analytics.search.es.SearchFieldFilter;
import com.enablix.analytics.search.es.SearchHitTransformer;
import com.enablix.analytics.search.es.TypeFilter;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;
import com.enablix.services.util.TemplateUtil;

@RestController
@RequestMapping("content")
public class LinkedContentController {

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
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/le/{contentQId}/{attrId}/{attrVal}/lt/{lookupContentQId}/", 
			produces = "application/json")
	public List<LinkedContent> getSpecificLinkedContent(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal, @PathVariable String lookupContentQId) {
		return fetchLinkedContent(contentQId, attrId, attrVal, lookupContentQId);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/le/{contentQId}/{attrId}/{attrVal}", 
			produces = "application/json")
	public List<LinkedContent> getLinkedContent(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal) {
		
		return fetchLinkedContent(contentQId, attrId, attrVal, null);
	}
		
	private List<LinkedContent> fetchLinkedContent(String contentQId, String attrId, String attrVal, String lookupContentQId) {
		
		List<LinkedContent> linkedContents = new ArrayList<>();
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		ContainerType containerDef = template.getContainerDefinition(contentQId);
		
		if (containerDef != null) {
		
			String instanceIdentity = null;
			
			String collectionName = template.getCollectionName(contentQId);
			StringFilter filter = new StringFilter(attrId, attrVal, ConditionOperator.EQ);
			
			DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
			MongoDataView mdbView = DataViewUtil.getMongoDataView(userDataView);
			
			// find the content record matching the attribute input
			List<Map<String, Object>> findRecords = contentCrud.findRecords(collectionName, filter, mdbView);
			
			for (Map<String, Object> rec : findRecords) {
				instanceIdentity = (String) rec.get(ContentDataConstants.IDENTITY_KEY);
			}
			
			if (instanceIdentity != null) {
				// find the linked containers for this content type
				LookupConfig lookupConfig = new LookupConfig();
				
				if (containerDef.getContainer() != null) {
					
					containerDef.getContainer().forEach((linkedContainer) -> {
					
						if (TemplateUtil.isLinkedContainer(linkedContainer)) {
							String linkContainerQId = linkedContainer.getLinkContainerQId();
							String linkedCollName = template.getCollectionName(linkContainerQId);
							lookupConfig.addLookupConfig(linkContainerQId, linkedCollName, linkedContainer.getLinkContentItemId());
						}
						
					});
				}
				
				TermsBuilder recordCountAgg = AggregationBuilders.terms("record_count");
				recordCountAgg.field("_type");
				recordCountAgg.size(50);
				
				SearchRequest searchRequest = ESQueryBuilder.builder(instanceIdentity, template, lookupConfig)
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
				
				
				String lookupColl = null;
				
				if (StringUtil.hasText(lookupContentQId)) {
					lookupColl = template.getCollectionName(lookupContentQId);
				}

				
				LinkedContent selectedLinkedContent = null;
				
				for (ContainerType linkedCont: containerDef.getContainer()) {
					
					if (TemplateUtil.isLinkedContainer(linkedCont)) {
					
						String linkContQId = linkedCont.getLinkContainerQId();
						String linkCollName = template.getCollectionName(linkContQId);
						
						if (collRecordCnt.containsKey(linkCollName)) {
						
							LinkedContent linkContent = new LinkedContent();
							
							linkContent.setContentQId(linkContQId);
							linkContent.setContentLabel(linkedCont.getLabel());
							
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
					
					SearchRequest recSearchRequest = ESQueryBuilder.builder(instanceIdentity, template, containerLC)
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
						
						// TODO: correct the usage of email address below
						docUrlPopulator.populateUnsecureUrl(dispRecord, "support@enablix.com");
						textLinkProcessor.process(dispRecord, template, "support@enablix.com");
						displayRecords.add(dispRecord);
					}
					
					if (selectedLinkedContent != null) {
						selectedLinkedContent.setRecords(displayRecords);
					}
					
				}
				
			}
		}
		
		return linkedContents;
	}
	
	public static class LinkedContent implements Comparable<LinkedContent> {
		
		private String contentQId;
		private String contentLabel;
		private boolean selected;
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
