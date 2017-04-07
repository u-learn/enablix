package com.enablix.trigger.lifecycle.action.email;

import org.springframework.stereotype.Component;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.AttributeCheckType;
import com.enablix.core.commons.xsdtopojo.AttributeUpdateType;
import com.enablix.core.domain.content.ContentChangeDelta;
import com.enablix.core.domain.trigger.ContentChange;

@Component
public class TriggerExecAttrUpdateChecker {

	/**
	 * Return <code>null</code>, <code>true</code> or <code>false</code>. 
	 * <code>null</code>, when no condition was specified for evaluation
	 * 
	 * @param contentChange
	 * @param attrUpdtCond
	 * @param template
	 * @return
	 */
	public Boolean evaluateCondition(ContentChange contentChange, 
			AttributeUpdateType attrUpdtCond, TemplateFacade template) {
		
		Boolean result = null;
		
		if (attrUpdtCond != null) {
		
			ContentChangeDelta delta = contentChange.getContentChange();
			
			// if any one of the on attr is present, then do change
			for (AttributeCheckType checkAttr : attrUpdtCond.getOn()) {
				
				if (delta.getChangedAttributes().containsKey(checkAttr.getName())) {
					result = true;
					break;
				}
				
			}

			// if any of the except attr is present, then do not change
			if (result == null || result == true) {
				for (AttributeCheckType checkAttr : attrUpdtCond.getExcept()) {
					if (delta.getChangedAttributes().containsKey(checkAttr.getName())) {
						result = false;
						break;
					}
				}
			}
			
		}
		
		return result;
	}
	
}
