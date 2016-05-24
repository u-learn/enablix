package com.enablix.core.correlation;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_item_item_correlation_rule")
public class ItemCorrelationRuleDocument extends BaseDocumentEntity {

	private ItemCorrelationRuleType itemCorrelationRule;

	public ItemCorrelationRuleType getItemCorrelationRule() {
		return itemCorrelationRule;
	}

	public void setItemCorrelationRule(ItemCorrelationRuleType itemCorrelationRule) {
		this.itemCorrelationRule = itemCorrelationRule;
	}
	
}
