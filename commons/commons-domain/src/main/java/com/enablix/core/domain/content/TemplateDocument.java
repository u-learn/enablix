package com.enablix.core.domain.content;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.BaseDocumentEntity;

@Document
public class TemplateDocument extends BaseDocumentEntity {

	private String filename;
	
	private ContentTemplate template;
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ContentTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ContentTemplate template) {
		this.template = template;
	}
	
}
