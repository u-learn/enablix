package com.enablix.content.quality.rule;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.content.quality.QualityRule;
import com.enablix.core.api.QualityRuleConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ParamSetType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.ContentObsoleteInfo;
import com.enablix.core.domain.content.quality.QualityAlert;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.ParamSetUtil;

@Component
public class ContentObsoleteVerificationRule implements QualityRule {

	@Override
	public String getRuleId() {
		return QualityRuleConstants.RULE_ID_CONTENT_OBSOLETE_VERIFY;
	}

	@Override
	public Collection<QualityAlert> evaluate(ParamSetType ruleConfig, Map<String, Object> contentRecord,
			String contentQId, TemplateFacade template) {
		
		ContainerType container = template.getContainerDefinition(contentQId);
		
		if (container != null) {
			
			Integer obsoleteInDays = ParamSetUtil.getIntegerValue(
					container.getQualityConfig(), QualityRuleConstants.PARAM_NAME_OBSOLETE_IN_DAYS);
			
			if (obsoleteInDays != null) {
		
				Date contentModifiedAt = ContentDataUtil.getContentModifiedAt(contentRecord);
				
				Calendar modifiedAtCal = Calendar.getInstance();
				modifiedAtCal.setTime(contentModifiedAt);
				modifiedAtCal.add(Calendar.DAY_OF_YEAR, obsoleteInDays);
				
				Calendar today = Calendar.getInstance();
				
				if (today.compareTo(modifiedAtCal) > 0) {
					
					// content obsolete
					ContentObsoleteInfo alertInfo = new ContentObsoleteInfo();
					alertInfo.setObsoleteOn(modifiedAtCal.getTime());
					
					return Collections.singletonList(QualityAlert.build(
							getRuleId(), alertInfo, AlertLevel.VERIFY, "Content due for verification"));
				}
			}
		}
		
		return null;
	}

	@Override
	public AlertLevel getAlertLevel() {
		return AlertLevel.VERIFY;
	}

}
