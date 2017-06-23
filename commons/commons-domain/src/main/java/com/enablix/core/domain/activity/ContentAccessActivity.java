package com.enablix.core.domain.activity;

public class ContentAccessActivity extends ContentActivity {

	protected ContentAccessActivity() {
		// for ORM
	}
	
	public ContentAccessActivity(String itemIdentity, String containerQId, ContainerType containerType, String itemTitle) {
		this(itemIdentity, containerQId, containerType, null, null, null, itemTitle);
	}

	public ContentAccessActivity(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm, String itemTitle) {

		super(itemIdentity, containerQId, containerType, ActivityType.CONTENT_ACCESS, itemTitle);
		
		setContextName(contextName);
		setContextId(contextId);
		setContextTerm(contextTerm);
	}

}
