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
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.TemplateUtil;

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
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/te/{contentQId}/{attrId}/{attrVal}", 
			produces = "application/json")
	public List<DisplayableContent> getCorrelatedEntities(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String contentQId, @PathVariable String attrId, 
			@PathVariable String attrVal) {
		
		String templateId = ProcessContext.get().getTemplateId();
		ContentTemplate template = templateMgr.getTemplate(templateId);
		
		String instanceIdentity = null;
		
		if (attrId.equals(ContentDataConstants.IDENTITY_KEY)) {
			
			instanceIdentity = attrVal;
			
		} else {
			
			String collectionName = TemplateUtil.resolveCollectionName(template, contentQId);
			StringFilter filter = new StringFilter(attrId, attrVal, ConditionOperator.EQ);
			
			List<Map<String, Object>> findRecords = contentCrud.findRecords(collectionName, filter);
			
			for (Map<String, Object> rec : findRecords) {
				instanceIdentity = (String) rec.get(ContentDataConstants.IDENTITY_KEY);
			}
		}
		
		List<String> filterTags = new ArrayList<>();
		String[] tags = request.getParameterValues("tag");
		if (tags != null) {
			filterTags = Arrays.asList(tags);
		}
		
		ContentDataRef triggerItem = new ContentDataRef(templateId, contentQId, instanceIdentity);
		List<ContentDataRecord> correlatedEntities = itemCorrService.fetchCorrelatedEntityRecords(
				template, triggerItem, new ArrayList<String>(), filterTags);
		
		DisplayContext ctx = new DisplayContext();
		
		List<DisplayableContent> displayRecords = new ArrayList<>();
		for (ContentDataRecord record : correlatedEntities) {
			DisplayableContent dispRecord = contentBuilder.build(template, record, ctx);
			docUrlPopulator.process(dispRecord, "support@enablix.com");
			textLinkProcessor.process(dispRecord, template, "support@enablix.com");
			displayRecords.add(dispRecord);
		}
		
		return displayRecords;
	}
	
}
