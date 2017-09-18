package com.enablix.content.quality.rule;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ui.FieldValueBuilder;
import com.enablix.app.content.ui.FieldValueBuilderFactory;
import com.enablix.commons.caching.api.CachingService;
import com.enablix.content.quality.rule.ContentAttributeQualityRule.AttrCheckConfig;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.quality.AlertLevel;

public abstract class RequiredAttributeRule extends ContentAttributeQualityRule<AttrCheckConfig> {

	@Autowired
	private FieldValueBuilderFactory fvBuilderFactory;
	
	@Autowired
	protected CachingService cache;
	
	public RequiredAttributeRule(String ruleId, AlertLevel alertLevel) {
		super(ruleId, alertLevel);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	protected boolean isAttributeQualityGood(Map<String, Object> contentRecord, 
			String contentQId, TemplateFacade template, ContentItemType attrDef) {
		
		Object itemValue = contentRecord.get(attrDef.getId());
		if (itemValue == null) {
			return false;
		}
		
		FieldValueBuilder builder = fvBuilderFactory.getBuilder(attrDef);
		return builder == null || !builder.isEmptyValue(attrDef, itemValue, template);
	}

}
