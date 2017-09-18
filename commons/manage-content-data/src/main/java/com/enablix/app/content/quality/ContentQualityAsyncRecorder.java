package com.enablix.app.content.quality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.content.quality.QualityAnalyzer;
import com.enablix.content.quality.QualityAnalyzer.AnalysisRuleSet;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.mq.EventSubscription;

@Component
public class ContentQualityAsyncRecorder {

	@Autowired
	private QualityAnalyzer analyzer;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@EventSubscription(eventName = {Events.CONTENT_CHANGE_EVENT})
	public void analyzeAndPersistAlerts(ContentDataSaveEvent event) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(event.getTemplateId());
		String contentQId = event.getContainerType().getQualifiedId();
		
		analyzer.analyzeAndRecord(event.getDataAsMap(), contentQId, AnalysisRuleSet.ALL, template);

	}
	
}
