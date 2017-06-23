package com.enablix.core.domain.activity;

public class ExternalLinkAccess extends NavigationActivity {

	private String externalUrl;
	
	private String contentIdentity; // content instant identity where the link was part of data
	
	private String contentItemQId; // QId of the attribute where the link was part of the data

	public ExternalLinkAccess(String externalUrl, String contentIdentity, String contentItemQId) {
		super(ActivityType.NAV_EXTERNAL_LINK, "EXT_LINK");
		this.externalUrl = externalUrl;
		this.contentIdentity = contentIdentity;
		this.contentItemQId = contentItemQId;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setContentIdentity(String contentIdentity) {
		this.contentIdentity = contentIdentity;
	}

	public String getContentItemQId() {
		return contentItemQId;
	}

	public void setContentItemQId(String contentItemQId) {
		this.contentItemQId = contentItemQId;
	}

}
