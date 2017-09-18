package com.enablix.content.quality.rule;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ParamSetType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.QualityAlert.AlertSeverity;
import com.enablix.services.util.ParamSetUtil;

@Component
public class ImportantAttributeCheck extends RequiredAttributeRule {

	public ImportantAttributeCheck() {
		super(RuleConstants.RULE_ID_REQUIRED_ATTR_CHECK, AlertLevel.WARN);
	}

	@Override
	protected String buildAlertMessage(AttrCheckConfig attrConfig) {
		return attrConfig.getAttrDef().getLabel() + " is required.";
	}
	
	
	protected AttrCheckConfig buildAttrCheckConfig(ContainerType container, ContentItemType item) {
		
		AttrCheckConfig attrConfig = null;
		
		ParamSetType paramSet = ParamSetUtil.getParamSet(item.getQualityConfig(), RuleConstants.PARAMSET_NAME_REQUIRED);
	
		if (paramSet != null) {
		
			String severity = ParamSetUtil.getStringValue(paramSet, RuleConstants.PARAM_NAME_SEVERITY);
			
			if (StringUtil.hasText(severity)) {
				attrConfig = new AttrCheckConfig(item, container.getQualifiedId(), 
						getAlertLevel(), AlertSeverity.valueOf(severity));
			}
		}
		
		return attrConfig;
	}

}
