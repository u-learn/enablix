package com.enablix.core.domain.ui;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_ui_widget_def")
public class UIWidgetDefinition extends BaseDocumentEntity {

	public enum Type {
		CONTENT_PACK
	}
	
	private String title;
	
	private Type type;
	
	private Map<String, Object> properties;

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

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
