package com.enablix.app.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.rule.ItemCorrelationRuleManager;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentCorrelatedItemType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;

@RestController
@RequestMapping("corr")
public class CorrelatedEntitiesController {

	@Autowired
	private ItemCorrelationService itemCorrService;
	
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
	private ItemCorrelationRuleManager corrRuleManager;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/te/{contentQId}/{attrId}/{attrVal}", 
			produces = "application/json")
	public List<DisplayableContent> getCorrelatedEntities(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		String instanceIdentity = null;
		String contentTitle = null;
		
		String collectionName = template.getCollectionName(contentQId);
		StringFilter filter = new StringFilter(attrId, attrVal, ConditionOperator.EQ);
		
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		MongoDataView mdbView = DataViewUtil.getMongoDataView(userDataView);
		
		List<Map<String, Object>> findRecords = contentCrud.findRecords(collectionName, filter, mdbView);
		
		for (Map<String, Object> rec : findRecords) {
			instanceIdentity = (String) rec.get(ContentDataConstants.IDENTITY_KEY);
			contentTitle = ContentDataUtil.findPortalLabelValue(rec, template, contentQId);
		}
		
		List<String> filterTags = new ArrayList<>();
		String[] tags = request.getParameterValues("tag");
		if (tags != null) {
			filterTags = Arrays.asList(tags);
		}
		
		ContentDataRef triggerItem = ContentDataRef.createContentRef(templateId, contentQId, instanceIdentity, contentTitle);
		List<ContentDataRecord> correlatedEntities = itemCorrService.fetchCorrelatedEntityRecords(
				template, triggerItem, new ArrayList<String>(), filterTags, userDataView);
		
		DisplayContext ctx = new DisplayContext();
		
		List<DisplayableContent> displayRecords = new ArrayList<>();
		for (ContentDataRecord record : correlatedEntities) {
			DisplayableContent dispRecord = contentBuilder.build(template, record, ctx);
			// TODO: correct the usage of email address below
			docUrlPopulator.populateUnsecureUrl(dispRecord, "support@enablix.com", ctx);
			textLinkProcessor.process(dispRecord, template, "support@enablix.com", ctx);
			displayRecords.add(dispRecord);
		}
		
		return displayRecords;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/itemtypes/{sourceItemQId}/", produces = "application/json")
	public List<ContentCorrelatedItemType> getCorrelatedItemTypeHierarchy(@PathVariable String sourceItemQId) {
		return corrRuleManager.getContentCorrelatedItemTypeHierarchy(sourceItemQId, 2);
	}
	
}
