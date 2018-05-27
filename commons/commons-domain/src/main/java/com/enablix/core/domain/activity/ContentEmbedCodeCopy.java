package com.enablix.core.domain.activity;

public class ContentEmbedCodeCopy extends ContentActivity{


	protected ContentEmbedCodeCopy() {
	}
	
	public ContentEmbedCodeCopy(String itemIdentity, String containerQId, ContainerType containerType, String itemTitle) {
		this(itemIdentity, containerQId, containerType, null, null, null, itemTitle);
	}

	public ContentEmbedCodeCopy(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm, String itemTitle) {
		
		super(itemIdentity, containerQId, containerType, ActivityType.CONTENT_EMBED_CODE_COPIED, itemTitle);
		
		setContextName(contextName);
		setContextId(contextId);
		setContextTerm(contextTerm);
	}
}
