package com.enablix.content.quality.rule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.content.quality.QualityRule;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mq.EventSubscription;

@Component
public class TemplateBaseRuleUpdater {

	@Autowired
	private QualityRuleFactory ruleFactory;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@EventSubscription(eventName = {Events.CONTENT_TEMPLATE_UPDATED})
	public void notifyRulesOnTemplateUpdate(ContentTemplate template) {
		
		TemplateFacade templateFacade = templateMgr.getTemplateFacade(template.getId());
		
		Collection<QualityRule> rules = ruleFactory.getAllRules();
		
		rules.forEach((rule) -> {
			if (rule instanceof TemplateConfiguredRule) {
				((TemplateConfiguredRule) rule).onTemplateUpdate(templateFacade);
			}
		});
	}
	
}
