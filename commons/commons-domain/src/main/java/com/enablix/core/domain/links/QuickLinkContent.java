package com.enablix.core.domain.links;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_quick_links")
public class QuickLinkContent extends BaseDocumentEntity {

	private String sectionName;
	
	private ContentDataRef data;

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public ContentDataRef getData() {
		return data;
	}

	public void setData(ContentDataRef data) {
		this.data = data;
	}
	
}
