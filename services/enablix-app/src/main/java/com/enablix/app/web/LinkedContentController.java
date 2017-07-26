package com.enablix.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
			value="/le/{contentQId}/{attrId}/{attrVal}", 
			produces = "application/json")
	public List<DisplayableContent> getLinkedContent(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal) {
		
		int pageSize = 10;
		int pageNum = 0;
		
		List<DisplayableContent> displayRecords = new ArrayList<>();
		
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
			
			SearchRequest searchRequest = ESQueryBuilder.builder(instanceIdentity, template, lookupConfig)
					.withPagination(pageSize, pageNum)
					.withFuzziness((searchTerm) -> null)
					.withTypeFilter(lookupConfig)
					.withFieldFilter(lookupConfig)
					.withOptimizer((mmQueryBuilder) -> mmQueryBuilder.minimumShouldMatch("100%"))
					.build();
	
			SearchHits result = esClient.searchContent(searchRequest);
			
			List<ContentDataRecord> linkedContent = new ArrayList<>();
			for (SearchHit hit : result) {
				ContentDataRecord linkedRecord = searchHitTx.toContentDataRecord(hit, template);
				linkedContent.add(linkedRecord);
			}
			
			
			DisplayContext ctx = new DisplayContext();
			
			for (ContentDataRecord record : linkedContent) {
				
				DisplayableContent dispRecord = contentBuilder.build(template, record, ctx);
				
				// TODO: correct the usage of email address below
				docUrlPopulator.populateUnsecureUrl(dispRecord, "support@enablix.com");
				textLinkProcessor.process(dispRecord, template, "support@enablix.com");
				displayRecords.add(dispRecord);
			}

		}
		
		return displayRecords;
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
			List<String> searchFields = new ArrayList<>();
			containerQIdToFieldId.values().forEach((fieldId) -> searchFields.add(fieldId + ".id"));
			return searchFields.toArray(new String[0]);
		}
	}
	
}
