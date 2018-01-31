package com.enablix.core.domain.activity;

public class ContentExtLinkURLCopy extends ContentActivity{


	protected ContentExtLinkURLCopy() {
	}
	
	public ContentExtLinkURLCopy(String itemIdentity, String containerQId, ContainerType containerType, String itemTitle) {
		this(itemIdentity, containerQId, containerType, null, null, null, itemTitle);
	}

	public ContentExtLinkURLCopy(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm, String itemTitle) {
		
		super(itemIdentity, containerQId, containerType, ActivityType.CONTENT_EXT_LINK_URL_COPIED, itemTitle);
		
		setContextName(contextName);
		setContextId(contextId);
		setContextTerm(contextTerm);
	}
}
