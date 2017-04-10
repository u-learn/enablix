package com.enablix.core.domain.segment;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_data_segment_spec")
public class DataSegmentSpec extends BaseDocumentEntity {

	private String templateId;
	
	private DataSegmentDefinitionType definition;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public DataSegmentDefinitionType getDefinition() {
		return definition;
	}

	public void setDefinition(DataSegmentDefinitionType definition) {
		this.definition = definition;
	}
	
}
