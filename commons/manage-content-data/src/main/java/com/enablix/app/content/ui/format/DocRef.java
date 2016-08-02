package com.enablix.app.content.ui.format;

public class DocRef extends AbstractFieldValue {

	private String name;
	
	private String docIdentity;
	
	private String accessUrl;

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

	@Override
	public String toString() {
		return "DocRef [name=" + name + ", docIdentity=" + docIdentity + "]";
	}

}
