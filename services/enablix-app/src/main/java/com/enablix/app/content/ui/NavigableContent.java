package com.enablix.app.content.ui;

public class NavigableContent {

	private String qualifiedId;
	
	private String identity;
	
	private String label;

	private NavigableContent next;
	
	public NavigableContent(String qualifiedId, String identity, String label) {
		super();
		this.qualifiedId = qualifiedId;
		this.identity = identity;
		this.label = label;
	}

	public String getQualifiedId() {
		return qualifiedId;
	}

	public String getIdentity() {
		return identity;
	}

	public String getLabel() {
		return label;
	}

	public void setQualifiedId(String qualifiedId) {
		this.qualifiedId = qualifiedId;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public NavigableContent getNext() {
		return next;
	}

	public void setNext(NavigableContent childContent) {
		this.next = childContent;
	}
	
}
