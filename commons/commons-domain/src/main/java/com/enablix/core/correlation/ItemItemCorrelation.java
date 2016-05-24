package com.enablix.core.correlation;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.Tag;
import com.enablix.core.api.Tagged;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_item_item_correlation")
public class ItemItemCorrelation extends BaseDocumentEntity implements Tagged {

	private ContentDataRef item;
	
	private ContentDataRef relatedItem;
	
	private String itemCorrelationRuleId;
	
	private float correlationScore;
	
	private Set<Tag> tags;
	
	public ContentDataRef getItem() {
		return item;
	}
	
	public void setItem(ContentDataRef item) {
		this.item = item;
	}
	
	public ContentDataRef getRelatedItem() {
		return relatedItem;
	}
	
	public void setRelatedItem(ContentDataRef relatedItem) {
		this.relatedItem = relatedItem;
	}
	
	public String getItemCorrelationRuleId() {
		return itemCorrelationRuleId;
	}
	
	public void setItemCorrelationRuleId(String itemCorrelationRuleId) {
		this.itemCorrelationRuleId = itemCorrelationRuleId;
	}

	public float getCorrelationScore() {
		return correlationScore;
	}
	
	public void setCorrelationScore(float correlationScore) {
		this.correlationScore = correlationScore;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

}
