package com.enablix.content.quality.rule;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.QualityAlert.AlertSeverity;
import com.enablix.services.util.ParamSetUtil;

@Component
public class MandatoryAttributeCheck extends RequiredAttributeRule {

	public MandatoryAttributeCheck() {
		super(RuleConstants.RULE_ID_REQUIRED_ATTR_VALIDATION, AlertLevel.ERROR);
	}

	@Override
	protected String buildAlertMessage(AttrCheckConfig attrConfig) {
		return attrConfig.getAttrDef().getLabel() + " is required.";
	}
	
	
	protected AttrCheckConfig buildAttrCheckConfig(ContainerType container, ContentItemType item) {
		
		Boolean required = ParamSetUtil.getBooleanValue(item.getQualityConfig(), RuleConstants.PARAM_NAME_REQUIRED, Boolean.FALSE);
		
		if (required) {
			return new AttrCheckConfig(item, container.getQualifiedId(), getAlertLevel(), AlertSeverity.HIGH);
		}
		
		return null;
	}

}
