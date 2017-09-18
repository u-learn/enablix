package com.enablix.content.quality;

import java.util.Collection;
import java.util.Map;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ParamSetType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.QualityAlert;

public interface QualityRule {

	String getRuleId();
	
	Collection<QualityAlert> evaluate(ParamSetType ruleConfig, 
		Map<String, Object> contentRecord, String contentQId, TemplateFacade template);
	
	AlertLevel getAlertLevel();
	
}
