package com.enablix.core.domain.content.pack;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_pack")
public class ContentPack extends BaseDocumentEntity {

	public enum Type {
		SELECTED_CONTENT
	}
	
	private String title;
	
	private Type type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}
