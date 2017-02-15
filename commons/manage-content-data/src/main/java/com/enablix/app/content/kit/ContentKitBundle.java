package com.enablix.app.content.kit;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;

public class ContentKitBundle {

	private ContentKitDetail contentKitDetail;
	
	private List<ContentDataRecord> contentRecords;
	
	public ContentKitDetail getContentKitDetail() {
		return contentKitDetail;
	}

	public void setContentKitDetail(ContentKitDetail linkedKitSummary) {
		this.contentKitDetail = linkedKitSummary;
	}

	public List<ContentDataRecord> getContentRecords() {
		return contentRecords;
	}

	public void setContentRecords(List<ContentDataRecord> contentRecords) {
		this.contentRecords = contentRecords;
	}
	
}
