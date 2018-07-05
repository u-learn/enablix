package com.enablix.content.quality.rule;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.content.quality.rule.ContentAttributeQualityRule.AttrCheckConfig;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.QualityRuleConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.QualityAlert.AlertSeverity;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.ParamSetUtil;

@Component
public class UniqueAttributeValueCheck extends DuplicateAttributeValueCheck<AttrCheckConfig> {

	public UniqueAttributeValueCheck() {
		super(QualityRuleConstants.RULE_ID_UNIQUE_ATTR_VALIDATION, AlertLevel.ERROR);
	}

	protected SearchFilter duplicateCheckFilter(ContentItemType attrDef, String itemValue, 
			Map<String, Object> contentRecord, String contentQId, TemplateFacade template) {
		
		String recIdentity = ContentDataUtil.getRecordIdentity(contentRecord);
		
		SearchFilter filter = new StringFilter(attrDef.getId(), itemValue, ConditionOperator.EQ);
		return filter.and(new StringFilter(ContentDataConstants.IDENTITY_KEY, recIdentity, ConditionOperator.NOT_EQ));
	}

	@Override
	protected String buildAlertMessage(AttrCheckConfig attrConfig) {
		return "Duplicate " + attrConfig.getAttrDef().getLabel() + " value.";
	}

	@Override
	protected AttrCheckConfig buildAttrCheckConfig(ContainerType container, ContentItemType item) {
		if (ParamSetUtil.isBooleanAndTrue(item.getQualityConfig(), QualityRuleConstants.PARAM_NAME_UNIQUE)) {
			return new AttrCheckConfig(item, container.getQualifiedId(), getAlertLevel(), AlertSeverity.HIGH);
		}
		return null;
	}
}
