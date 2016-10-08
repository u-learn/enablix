package com.enablix.core.domain.content.mapping;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_container_mapping")
public class ContentContainerMappingDocument extends BaseDocumentEntity {

	private String mapperId;
	
	private String mapperName;
	
	private ContentContainerMappingType containerMapping;

	public String getMapperId() {
		return mapperId;
	}

	public void setMapperId(String mapperId) {
		this.mapperId = mapperId;
	}

	public String getMapperName() {
		return mapperName;
	}

	public void setMapperName(String mapperName) {
		this.mapperName = mapperName;
	}

	public ContentContainerMappingType getContainerMapping() {
		return containerMapping;
	}

	public void setContainerMapping(ContentContainerMappingType containerMapping) {
		this.containerMapping = containerMapping;
	}

}
