package com.enablix.core.domain.ui;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_ui_widget_def")
public class UIWidgetDefinition extends BaseDocumentEntity {

	public enum Type {
		CONTENT_PACK
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
