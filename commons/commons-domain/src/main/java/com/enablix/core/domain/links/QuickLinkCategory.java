package com.enablix.core.domain.links;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.OrderedDocumentEntity;

@Document(collection = "ebx_quick_link_category")
public class QuickLinkCategory extends OrderedDocumentEntity {

	private String name;
	
	private String clientId;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String sectionName) {
		this.name = sectionName;
	}
	
}
