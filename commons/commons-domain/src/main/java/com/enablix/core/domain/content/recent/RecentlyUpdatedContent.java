package com.enablix.core.domain.content.recent;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.BaseDocumentEntity;

@Document
public class RecentlyUpdatedContent extends BaseDocumentEntity {

	public static enum UpdateType {
		NEW, UPDATED
	}
	
	private String baseContainerQId;
	
	private UpdateType updateType;
	
	private ContentDataRef updatedData;

	public String getBaseContainerQId() {
		return baseContainerQId;
	}

	public void setBaseContainerQId(String baseContainerQId) {
		this.baseContainerQId = baseContainerQId;
	}

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public ContentDataRef getUpdatedData() {
		return updatedData;
	}

	public void setUpdatedData(ContentDataRef updatedData) {
		this.updatedData = updatedData;
	}
	
}
