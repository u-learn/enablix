package com.enablix.core.domain.activity;

import com.enablix.commons.util.QIdUtil;

public class DocumentActivity extends ContentAccessActivity {

	private String docIdentity;
	
	private String docQId;
	
	private String docTitle;
	
	public String getDocQId() {
		return docQId;
	}

	public void setDocQId(String docContainerQId) {
		this.docQId = docContainerQId;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	protected DocumentActivity() { 
		// for ORM
	}
	
	public DocumentActivity(ActivityType activityType, String itemIdentity, String docQId, 
			ContainerType containerType, String docIdentity, String itemTitle, String docTitle) {
		this(activityType, itemIdentity, docQId, containerType, docIdentity, null, null, null, itemTitle, docTitle);
	}

	public DocumentActivity(ActivityType activityType, String itemIdentity, String docQId, 
			ContainerType containerType, String docIdentity, String contextName, String contextId, 
			String contextTerm, String itemTitle, String docTitle) {
		
		super(itemIdentity, docQId != null ? QIdUtil.getParentQId(docQId) : null, containerType, contextName, contextId, contextTerm, itemTitle);
		
		this.docIdentity = docIdentity;
		this.docTitle = docTitle;
		this.docQId = docQId;
		setActivityType(activityType);
	}

	
	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}
	
}
