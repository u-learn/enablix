package com.enablix.content.quality.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.quality.rule.ContentAttributeQualityRule.AttrCheckConfig;
import com.enablix.content.quality.rule.DuplicateAcrossAttrCheck.MatchAgainstAttrConfig;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.QualityRuleConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ParamSetType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.QualityAlert.AlertSeverity;
import com.enablix.core.mongo.MongoUtil;
import com.enablix.core.mongo.search.RegexFilter;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.ParamSetUtil;

@Component
public class DuplicateAcrossAttrCheck extends DuplicateAttributeValueCheck<MatchAgainstAttrConfig> {

	public DuplicateAcrossAttrCheck() {
		super(QualityRuleConstants.RULE_ID_DUPLICATE_VALUE_CHECK, AlertLevel.WARN);
	}

	protected SearchFilter duplicateCheckFilter(ContentItemType attrDef, String itemValue, 
			Map<String, Object> contentRecord, String contentQId, TemplateFacade template) {
		
		ParamSetType dupCheckParams = ParamSetUtil.getParamSet(
				attrDef.getQualityConfig(), QualityRuleConstants.PARAMSET_NAME_DUPLICATION_CHECK);
		
		Collection<String> dupAttrs = ParamSetUtil.getStringValues(dupCheckParams, QualityRuleConstants.PARAM_NAME_MATCH_ATTR);
		
		String recIdentity = ContentDataUtil.getRecordIdentity(contentRecord);
		
		SearchFilter filter = null;
		Pattern caseInsensitiveMatchValue = MongoUtil.caseInsensitiveMatchRegexPattern(itemValue);
		
		for (String dupAttr : dupAttrs) {
			if (filter == null) {
				filter = new RegexFilter(dupAttr, caseInsensitiveMatchValue, ConditionOperator.REGEX);
			} else {
				filter = filter.or(new RegexFilter(dupAttr, caseInsensitiveMatchValue, ConditionOperator.REGEX));
			}
		};

		if (filter == null) {
			// fallback in case there are no attrs defined to look for duplicate value
			// so that no record matches
			filter = new StringFilter(attrDef.getId(), "~~asd@312~~", ConditionOperator.EQ);
		}
		
		return filter.and(new StringFilter(ContentDataConstants.IDENTITY_KEY, recIdentity, ConditionOperator.NOT_EQ));
	}

	@Override
	protected String buildAlertMessage(MatchAgainstAttrConfig attrConfig) {
		return "Duplicate " + attrConfig.getAttrDef().getLabel() + " value.";
	}

	@Override
	protected MatchAgainstAttrConfig buildAttrCheckConfig(ContainerType container, ContentItemType item) {
		
		ParamSetType paramSet = ParamSetUtil.getParamSet(
				item.getQualityConfig(), QualityRuleConstants.PARAMSET_NAME_DUPLICATION_CHECK);
		
		if (paramSet != null) {
			
			Collection<String> dupAttrs = ParamSetUtil.getStringValues(paramSet, QualityRuleConstants.PARAM_NAME_MATCH_ATTR);
			
			if (CollectionUtil.isNotEmpty(dupAttrs)) {
				String severity = ParamSetUtil.getStringValue(paramSet, QualityRuleConstants.PARAM_NAME_SEVERITY);
				AlertSeverity alertSeverity = StringUtil.hasText(severity) ? AlertSeverity.valueOf(severity) : AlertSeverity.MEDIUM;
			
				return new MatchAgainstAttrConfig(item, container.getQualifiedId(), getAlertLevel(), alertSeverity, dupAttrs);
			}
		}
		
		return null;
	}
	
	public static class MatchAgainstAttrConfig extends AttrCheckConfig {

		private List<String> matchAttrs = new ArrayList<>();
		
		public MatchAgainstAttrConfig(ContentItemType attrDef, String containerQId, AlertLevel alertLevel,
				AlertSeverity alertSeverity, Collection<String> dupAttrs) {
			super(attrDef, containerQId, alertLevel, alertSeverity);
			addMatchAttrs(dupAttrs);
		}
		
		public void addMatchAttrs(Collection<String> matchAttrs) {
			this.matchAttrs.addAll(matchAttrs);
		}
		
	}

}
