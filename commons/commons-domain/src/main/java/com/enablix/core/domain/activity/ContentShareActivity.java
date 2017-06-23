package com.enablix.core.domain.activity;

public class ContentShareActivity extends ContentActivity {

	public enum ShareMedium {
		WEB, WEEKLY_DIGEST, CORRELATION
	}
	
	private String sharingId; // identifier for sharing action 
	
	private ShareMedium sharedFrom;
	
	private String sharedWith;
	
	public ContentShareActivity(String itemIdentity, String containerQId, 
			ContainerType containerType, String sharingId, ShareMedium sharedFrom, 
			String sharedWith, String itemTitle) {
		
		super(itemIdentity, containerQId, containerType, ActivityType.CONTENT_SHARE, itemTitle);
		
		this.sharingId = sharingId;
		this.sharedFrom = sharedFrom;
		this.sharedWith = sharedWith;
	}

	public String getSharingId() {
		return sharingId;
	}

	public void setSharingId(String sharingId) {
		this.sharingId = sharingId;
	}

	public ShareMedium getSharedFrom() {
		return sharedFrom;
	}

	public void setSharedFrom(ShareMedium sharedFrom) {
		this.sharedFrom = sharedFrom;
	}

	public String getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(String sharedWith) {
		this.sharedWith = sharedWith;
	}

}
