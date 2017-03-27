package com.enablix.core.correlation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.Tag;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = AppConstants.ITEM_USER_CORR_COLL_NAME)
public class ItemUserCorrelation extends BaseDocumentEntity {

	private ContentDataRef item;
	
	private String userProfileIdentity;
	
	@SuppressWarnings("rawtypes")
	private List<CorrelationSource> sources;
	
	private float correlationScore;
	
	/*
	 * Tags can be added for various usage e.g. identify DIRECT v/s DERIVED relation
	 * We can then define rules to work with DIRECT relationship and if not found, then
	 * work with the DERIVED relationship.
	 */
	private Set<Tag> tags;

	public ContentDataRef getItem() {
		return item;
	}

	public void setItem(ContentDataRef item) {
		this.item = item;
	}

	public String getUserProfileIdentity() {
		return userProfileIdentity;
	}

	public void setUserProfileIdentity(String userIdentity) {
		this.userProfileIdentity = userIdentity;
	}

	@SuppressWarnings("rawtypes")
	public List<CorrelationSource> getSources() {
		return sources;
	}

	@SuppressWarnings("rawtypes")
	public void setSources(List<CorrelationSource> sources) {
		this.sources = sources;
	}
	
	public void addSource(CorrelationSource<?> source) {
		if (sources == null) {
			sources = new ArrayList<>();
			sources.add(source);
		} else {
			if (!sources.contains(source)) {
				sources.add(source);
			}
		}
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
