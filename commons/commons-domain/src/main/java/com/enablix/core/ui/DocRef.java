package com.enablix.core.ui;

public class DocRef extends AbstractFieldValue {

	private String name;
	
	private String docIdentity;
	
	private String accessUrl;
	
	private String thumbnailUrl;

	public DocRef() {
		super(FieldValueType.DOC);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@Override
	public String toString() {
		return "DocRef [name=" + name + ", docIdentity=" + docIdentity + "]";
	}

}
