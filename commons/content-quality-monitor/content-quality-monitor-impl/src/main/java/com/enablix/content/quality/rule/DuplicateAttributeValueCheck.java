package com.enablix.content.quality.rule;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.quality.rule.ContentAttributeQualityRule.AttrCheckConfig;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.view.MongoDataView;

public abstract class DuplicateAttributeValueCheck<C extends AttrCheckConfig> extends ContentAttributeQualityRule<C> {

	@Autowired
	private ContentCrudService crud;
	
	public DuplicateAttributeValueCheck(String ruleId, AlertLevel alertLevel) {
		super(ruleId, alertLevel);
	}

	protected boolean isAttributeQualityGood(Map<String, Object> contentRecord, 
			String contentQId, TemplateFacade template, ContentItemType attrDef) {
		
		if (attrDef.getType() == ContentItemClassType.TEXT) {
			
			String itemValue = (String) contentRecord.get(attrDef.getId());
			
			
			if (StringUtil.hasText(itemValue)) {
			
				String collectionName = template.getCollectionName(contentQId);
				
				SearchFilter filter = duplicateCheckFilter(attrDef, itemValue, contentRecord, contentQId, template);
				
				List<Map<String, Object>> dupRecords = crud.findRecords(collectionName, filter, MongoDataView.ALL_DATA);
				return CollectionUtil.isEmpty(dupRecords);
			}
		}
		
		return true;
	}

	protected abstract SearchFilter duplicateCheckFilter(ContentItemType attrDef, String itemValue, 
			Map<String, Object> contentRecord, String contentQId, TemplateFacade template);

}
