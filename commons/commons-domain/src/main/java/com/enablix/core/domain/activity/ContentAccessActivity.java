package com.enablix.core.domain.activity;

public class ContentAccessActivity extends ContentActivity {

	private String contextName; // if part of some campaign
	
	private String contextId; // campaign id
	
	private String contextTerm; // addition term to track
	
	protected ContentAccessActivity() {
		// for ORM
	}
	
	public ContentAccessActivity(String itemIdentity, String containerQId, ContainerType containerType) {
		this(itemIdentity, containerQId, containerType, null, null, null);
	}

	public ContentAccessActivity(String itemIdentity, String containerQId, ContainerType containerType,
			String contextName, String contextId, String contextTerm) {

		super(itemIdentity, containerQId, containerType, ContentActivityType.CONTENT_ACCESS);
		
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
