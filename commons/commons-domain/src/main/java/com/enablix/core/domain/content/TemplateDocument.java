package com.enablix.core.domain.content;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.BaseDocumentEntity;

@Document
public class TemplateDocument extends BaseDocumentEntity {

	private ContentTemplate template;

	public ContentTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ContentTemplate template) {
		this.template = template;
	}
	
}
