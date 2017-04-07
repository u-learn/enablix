package com.enablix.core.domain.links;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.DataSegmentAwareEntity;

@Document(collection = "ebx_quick_links")
public class QuickLinkContent extends DataSegmentAwareEntity {

	@DBRef
	private QuickLinkCategory category;
	
	private ContentDataRef data;
	
	public QuickLinkCategory getCategory() {
		return category;
	}

	public void setCategory(QuickLinkCategory category) {
		this.category = category;
	}

	public ContentDataRef getData() {
		return data;
	}

	public void setData(ContentDataRef data) {
		this.data = data;
	}

}
