package com.enablix.core.correlation;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.Tag;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_item_user_correlation")
public class ItemUserCorrelation extends BaseDocumentEntity {

	private ContentDataRef item;
	
	private ContentDataRef user;
	
	@SuppressWarnings("rawtypes")
	private List<CorrelationSource> sources;
	
	private float correlationScore;
	
	/*
	 * Tags can be added for various usage e.g. identify DIRECT v/s DERIVED relation
	 * We can then define rules to work with DIRECT relationship and if not found, then
	 * work with the DERIVED relationship.
	 */
	private List<Tag> tags;

	public ContentDataRef getItem() {
		return item;
	}

	public void setItem(ContentDataRef item) {
		this.item = item;
	}

	public ContentDataRef getUser() {
		return user;
	}

	public void setUser(ContentDataRef user) {
		this.user = user;
	}

	@SuppressWarnings("rawtypes")
	public List<CorrelationSource> getSources() {
		return sources;
	}

	@SuppressWarnings("rawtypes")
	public void setSources(List<CorrelationSource> sources) {
		this.sources = sources;
	}

	public float getCorrelationScore() {
		return correlationScore;
	}

	public void setCorrelationScore(float correlationScore) {
		this.correlationScore = correlationScore;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
}
