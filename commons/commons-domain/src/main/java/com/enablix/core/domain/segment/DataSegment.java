package com.enablix.core.domain.segment;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_data_segment")
public class DataSegment extends BaseDocumentEntity {
	
	private String name;
	
	private List<DataSegmentAttribute> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataSegmentAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<DataSegmentAttribute> attributes) {
		this.attributes = attributes;
	}
	
}
