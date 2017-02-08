package com.enablix.app.content.ui;

import java.util.HashMap;
import java.util.Map;

public class NavigableContent {

	private String qualifiedId;
	
	private String containerLabel;
	
	private String identity;
	
	private String label;
	
	private String docIdentity;
	
	private Map<String, String> additionalInfo;

	private NavigableContent next;

	private Map<String, Object> docDetails;
	
	public NavigableContent(String qualifiedId, String identity, String label, String containerLabel) {
		super();
		this.qualifiedId = qualifiedId;
		this.identity = identity;
		this.label = label;
		this.containerLabel = containerLabel;
		this.additionalInfo = new HashMap<>();
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

	public String getContainerLabel() {
		return containerLabel;
	}

	public void setContainerLabel(String containerLabel) {
		this.containerLabel = containerLabel;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}

	public NavigableContent getNext() {
		return next;
	}

	public void setNext(NavigableContent childContent) {
		this.next = childContent;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public void setDocDetails(Map<String, Object> docDetails) {
		this.docDetails = docDetails;
	}
	
	public Map<String, Object> getDocDetails() {
		return docDetails;
	}

	public String toPath(String pathSeparator) {
		
		String path = getLabel();
		
		if (next != null) {
			path += pathSeparator + next.toPath(pathSeparator);
		}
		
		return path;
	}

}
