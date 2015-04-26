package com.enablix.core.domain.content;

public class ContentAssociation {

	private String collectionName;
	private String recordIdentity;
	private String recordId;
	private String associationName;
	
	protected ContentAssociation() { }
	
	public ContentAssociation(String associationName, 
			String collectionName, String recordId, String recordIdentity) {
		super();
		this.collectionName = collectionName;
		this.recordId = recordId;
		this.recordIdentity = recordIdentity;
		this.associationName = associationName;
	}
	
	public String getAssociationName() {
		return associationName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public String getRecordId() {
		return recordId;
	}
	
	
}
