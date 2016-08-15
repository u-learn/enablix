package com.enablix.core.domain.activity;

public class DocDownload extends ContentAccessActivity {

	private String docIdentity;
	
	public DocDownload(String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity) {
		this(itemIdentity, containerQId, containerType, docIdentity, null, null);
	}

	public DocDownload(String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity, String campaign, String campaignId) {
		
		super(itemIdentity, containerQId, containerType, campaign, campaignId);
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
