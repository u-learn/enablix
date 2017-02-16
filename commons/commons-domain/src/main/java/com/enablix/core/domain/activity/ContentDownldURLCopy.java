package com.enablix.core.domain.activity;

public class ContentDownldURLCopy extends ContentActivity {

	private String contextName;
	
	private String contextId; 
	
	private String contextTerm;
	
	protected ContentDownldURLCopy() {
	}
	
	public ContentDownldURLCopy(String itemIdentity, String containerQId, ContainerType containerType, String itemTitle) {
		this(itemIdentity, containerQId, containerType, null, null, null, itemTitle);
	}

	public ContentDownldURLCopy(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm, String itemTitle) {
		super(itemIdentity, containerQId, containerType, ContentActivityType.CONTENT_DOWNLD_URL_COPIED, itemTitle);
		this.contextName = contextName;
		this.contextId = contextId;
		this.contextTerm = contextTerm;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String campaign) {
		this.contextName = campaign;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getContextTerm() {
		return contextTerm;
	}

	public void setContextTerm(String contextTerm) {
		this.contextTerm = contextTerm;
	}

}
