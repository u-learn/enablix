package com.enablix.core.domain.content.quality;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.DataSegmentAwareEntity;

@Document(collection = "ebx_content_quality_alert")
public class ContentQualityAlert extends DataSegmentAwareEntity implements ContentRecordRef {

	private String contentQId;
	
	private String recordIdentity;
	
	private String authorUserId;
	
	private String authorName;
	
	private QualityAlert alert;

	public ContentQualityAlert() {
		super();
	}

	public ContentQualityAlert(String contentQId, String recordIdentity, String authorUserId, String authorName,
			QualityAlert alert) {
		super();
		this.contentQId = contentQId;
		this.recordIdentity = recordIdentity;
		this.authorUserId = authorUserId;
		this.authorName = authorName;
		this.alert = alert;
	}


	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public void setRecordIdentity(String contentIdentity) {
		this.recordIdentity = contentIdentity;
	}
	
	public String getAuthorUserId() {
		return authorUserId;
	}

	public void setAuthorUserId(String authorUserId) {
		this.authorUserId = authorUserId;
	}
	
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public QualityAlert getAlert() {
		return alert;
	}

	public void setAlert(QualityAlert alert) {
		this.alert = alert;
	}

}
