package com.enablix.core.domain.activity;

public class DocumentActivity extends ContentAccessActivity {

	private String docIdentity;
	
	protected DocumentActivity() { 
		// for ORM
	}
	
	public DocumentActivity(ActivityType activityType, String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity, String itemTitle) {
		this(activityType, itemIdentity, containerQId, containerType, docIdentity, null, null, null, itemTitle);
	}

	public DocumentActivity(ActivityType activityType, String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity, String contextName, String contextId, 
			String contextTerm, String itemTitle) {
		
		super(itemIdentity, containerQId, containerType, contextName, contextId, contextTerm, itemTitle);
		this.docIdentity = docIdentity;
		setActivityType(activityType);
	}

	
	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}
	
}
