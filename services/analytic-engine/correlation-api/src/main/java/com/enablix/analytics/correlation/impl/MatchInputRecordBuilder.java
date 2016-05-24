package com.enablix.analytics.correlation.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.app.content.ContentDataManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;

@Component
public class MatchInputRecordBuilder {
	
	@Autowired
	private ContentCrudService dataService;
	
	@Autowired
	private ContentDataManager contentDataMgr;

	public MatchInputRecord buildTriggerMatchInput(ContentTemplate template, ContentDataRef triggerItem) {
		
		String collName = TemplateUtil.resolveCollectionName(template, triggerItem.getContainerQId());
		Map<String, Object> triggerItemRecord = dataService.findRecord(collName, triggerItem.getInstanceIdentity());
		
		MatchInputRecord matchInput = new MatchInputRecord(triggerItem.getContainerQId(), triggerItem.getContainerQId(), triggerItemRecord);
		
		populateParent(matchInput, template);
		
		return matchInput;
	}
	
	private void populateParent(MatchInputRecord matchInput, ContentTemplate template) {
		
		Map<String, Object> parentRecord = contentDataMgr.fetchParentRecord(
				template, matchInput.getContentQId(), matchInput.getRecord());
		
		if (parentRecord != null) {
			MatchInputRecord parentMI = new MatchInputRecord(matchInput.getTriggerItemQId(), 
					QIdUtil.getParentQId(matchInput.getContentQId()), parentRecord);
			matchInput.setParent(parentMI);
			
			populateParent(parentMI, template);
		}
		
	}
	
}
