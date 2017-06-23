package com.enablix.core.domain.activity;

public class ContentPortalURLCopy extends ContentActivity{


	protected ContentPortalURLCopy() {
	}
	
	public ContentPortalURLCopy(String itemIdentity, String containerQId, ContainerType containerType, String itemTitle) {
		this(itemIdentity, containerQId, containerType, null, null, null, itemTitle);
	}

	public ContentPortalURLCopy(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm, String itemTitle) {
		
		super(itemIdentity, containerQId, containerType, ActivityType.CONTENT_PORTAL_URL_COPIED, itemTitle);
		
		setContextName(contextName);
		setContextId(contextId);
		setContextTerm(contextTerm);
	}
}
