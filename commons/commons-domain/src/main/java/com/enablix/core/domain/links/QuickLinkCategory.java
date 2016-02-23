package com.enablix.core.domain.links;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_quick_link_category")
public class QuickLinkCategory extends BaseDocumentEntity {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String sectionName) {
		this.name = sectionName;
	}
	
}
