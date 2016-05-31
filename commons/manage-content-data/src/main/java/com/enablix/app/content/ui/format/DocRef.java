package com.enablix.app.content.ui.format;

public class DocRef implements FieldValue {

	private String name;
	
	private String docIdentity;
	
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

	@Override
	public FieldValueType getValueType() {
		return FieldValueType.DOC;
	}

	@Override
	public String toString() {
		return "DocRef [name=" + name + ", docIdentity=" + docIdentity + "]";
	}

}
