package com.enablix.core.domain.activity;

public class DocDownload extends ContentActivity {

	private String docIdentity;
	
	public DocDownload(String itemIdentity, String containerQId, ContainerType containerType,
			ContentActivityType activityType, String docIdentity) {
		super(itemIdentity, containerQId, containerType, activityType);
		this.docIdentity = docIdentity;
	}

	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}
	
}
