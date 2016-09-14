package com.enablix.core.domain.activity;

public class DocDownload extends ContentAccessActivity {

	private String docIdentity;
	
	protected DocDownload() { 
		// for ORM
	}
	
	public DocDownload(String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity) {
		this(itemIdentity, containerQId, containerType, docIdentity, null, null, null);
	}

	public DocDownload(String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity, String contextName, String contextId, String contextTerm) {
		
		super(itemIdentity, containerQId, containerType, contextName, contextId, contextTerm);
		this.docIdentity = docIdentity;
		setActivityType(ContentActivityType.DOC_DOWNLOAD);
	}

	
	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}
	
}
