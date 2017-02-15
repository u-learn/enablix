package com.enablix.trigger.lifecycle.action.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.TriggerEntityUpdateCondType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class TriggerExecChangeOnEntityUpdateEvaluator {

	@Autowired
	private TriggerExecAttrUpdateChecker attrUpdateChecker;
	
	@Autowired
	private TriggerExecEntityMatchChecker entityMatchChecker;
	
	public boolean evaluateChangeCondition(ContentChange contentChange, 
			TriggerEntityUpdateCondType condition, TemplateWrapper template) {
		
		Boolean updtAttrResult = attrUpdateChecker.evaluateCondition(
				contentChange, condition.getAttributeUpdate(), template);
		
		Boolean entityMatchResult = entityMatchChecker.evaluateCondition(
				contentChange, condition.getEntityMatch(), template);

		// if both are null, then the conditions were missing, hence no change
		return !(updtAttrResult == null && entityMatchResult == null)
				&& ((updtAttrResult == null || updtAttrResult) 
						&& (entityMatchResult == null || entityMatchResult));
	}
	
}
