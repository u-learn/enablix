package com.enablix.core.domain.recent;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.DataSegmentAwareEntity;

@Document(collection = "ebx_recent_update")
public class RecentData extends DataSegmentAwareEntity {

	public enum UpdateType { 
		NEW, UPDATED
	}
	
	private RecentDataScope scope;
	
	private ContentDataRef data;
	
	private UpdateType updateType;
	
	// If this is a record for UpdateType.NEW and has a corresponding UpdateType.UPDATE record,
	// then obsolete flag will be set
	private boolean obsolete;

	public RecentDataScope getScope() {
		return scope;
	}

	public ContentDataRef getData() {
		return data;
	}

	public void setScope(RecentDataScope scope) {
		this.scope = scope;
	}

	public void setData(ContentDataRef data) {
		this.data = data;
	}

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public boolean isObsolete() {
		return obsolete;
	}

	public void setObsolete(boolean obsolete) {
		this.obsolete = obsolete;
	}
	
}
