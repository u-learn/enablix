package com.enablix.core.correlation;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_item_user_correlation_rule")
public class ItemUserCorrelationRuleDocument extends BaseDocumentEntity {

	private ItemUserCorrelationRuleType itemUserCorrelationRule;

	public ItemUserCorrelationRuleType getItemUserCorrelationRule() {
		return itemUserCorrelationRule;
	}

	public void setItemUserCorrelationRule(ItemUserCorrelationRuleType itemUserCorrelationRule) {
		this.itemUserCorrelationRule = itemUserCorrelationRule;
	}
	
}
