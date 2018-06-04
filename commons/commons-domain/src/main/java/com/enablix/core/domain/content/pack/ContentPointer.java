package com.enablix.core.domain.content.pack;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.DataSegmentAwareEntity;

@Document(collection = "ebx_content_pointer")
public class ContentPointer extends DataSegmentAwareEntity {

	private String parentIdentity;
	
	private String parentType;
	
	private ContentDataRef data;

	public String getParentIdentity() {
		return parentIdentity;
	}

	public void setParentIdentity(String parentIdentity) {
		this.parentIdentity = parentIdentity;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public ContentDataRef getData() {
		return data;
	}

	public void setData(ContentDataRef data) {
		this.data = data;
	}
	
}
