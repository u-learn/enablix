package com.enablix.app.content.ui.format;

public class DocRef extends AbstractFieldValue {

	private String name;
	
	private String docIdentity;

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

	@Override
	public String toString() {
		return "DocRef [name=" + name + ", docIdentity=" + docIdentity + "]";
	}

}
