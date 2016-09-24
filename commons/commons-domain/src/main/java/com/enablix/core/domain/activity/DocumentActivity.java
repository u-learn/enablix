package com.enablix.core.domain.activity;

public class DocumentActivity extends ContentAccessActivity {

	private String docIdentity;
	
	protected DocumentActivity() { 
		// for ORM
	}
	
	public DocumentActivity(ContentActivityType activityType, String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity) {
		this(activityType, itemIdentity, containerQId, containerType, docIdentity, null, null, null);
	}

	public DocumentActivity(ContentActivityType activityType, String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity, String contextName, String contextId, String contextTerm) {
		
		super(itemIdentity, containerQId, containerType, contextName, contextId, contextTerm);
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
