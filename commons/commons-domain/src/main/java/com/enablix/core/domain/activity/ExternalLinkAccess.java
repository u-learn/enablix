package com.enablix.core.domain.activity;

public class ExternalLinkAccess extends RecordAwareNavActivity {

	private String externalUrl;
	
	protected ExternalLinkAccess() {
		// for ORM
	}
	
	public ExternalLinkAccess(String externalUrl, String itemIdentity, String containerQId) {
		super(ActivityType.NAV_EXTERNAL_LINK, "EXT_LINK", containerQId, itemIdentity, null);
		this.externalUrl = externalUrl;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

}
