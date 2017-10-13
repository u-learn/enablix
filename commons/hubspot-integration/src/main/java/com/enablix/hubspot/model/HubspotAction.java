package com.enablix.hubspot.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public abstract class HubspotAction {

	private String type;
	
	private String label;
	
	private List<String> associatedObjectProperties;

	public HubspotAction(String type, String label) {
		super();
		this.type = type;
		this.label = label;
		this.associatedObjectProperties = new ArrayList<>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getAssociatedObjectProperties() {
		return associatedObjectProperties;
	}

	public void setAssociatedObjectProperties(List<String> associatedObjectProperties) {
		this.associatedObjectProperties = associatedObjectProperties;
	}
	
}
