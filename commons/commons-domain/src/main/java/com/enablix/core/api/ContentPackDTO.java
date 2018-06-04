package com.enablix.core.api;

import org.springframework.data.domain.Page;

import com.enablix.core.domain.content.pack.ContentPack;

public class ContentPackDTO {

	private ContentPack pack;
	
	private Page<ContentDataRecord> records;

	public ContentPack getPack() {
		return pack;
	}

	public void setPack(ContentPack pack) {
		this.pack = pack;
	}

	public Page<ContentDataRecord> getRecords() {
		return records;
	}

	public void setRecords(Page<ContentDataRecord> records) {
		this.records = records;
	}
	
}
