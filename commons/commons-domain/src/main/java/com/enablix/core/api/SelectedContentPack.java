package com.enablix.core.api;

import java.util.List;

import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.core.domain.content.pack.ContentPointer;

public class SelectedContentPack {

	private ContentPack pack;
	
	private List<ContentPointer> records;

	public ContentPack getPack() {
		return pack;
	}

	public void setPack(ContentPack pack) {
		this.pack = pack;
	}

	public List<ContentPointer> getRecords() {
		return records;
	}

	public void setRecords(List<ContentPointer> records) {
		this.records = records;
	}

}
