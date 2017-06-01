package com.enablix.core.domain.activity;

public class ContentDownldURLCopy extends ContentActivity {

	protected ContentDownldURLCopy() {
	}
	
	public ContentDownldURLCopy(String itemIdentity, String containerQId, ContainerType containerType, String itemTitle) {
		this(itemIdentity, containerQId, containerType, null, null, null, itemTitle);
	}

	public ContentDownldURLCopy(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm, String itemTitle) {
		
		super(itemIdentity, containerQId, containerType, ContentActivityType.CONTENT_DOWNLD_URL_COPIED, itemTitle);
		
		setContextName(contextName);
		setContextId(contextId);
		setContextTerm(contextTerm);
	}

}
