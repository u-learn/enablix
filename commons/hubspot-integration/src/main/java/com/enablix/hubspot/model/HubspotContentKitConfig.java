package com.enablix.hubspot.model;

import java.util.Map;

public class HubspotContentKitConfig {

	private String containerQId;
	
	private String recordIdentity;
	
	private Map<String, String> matchAttributes;

	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public void setRecordIdentity(String recordIdentity) {
		this.recordIdentity = recordIdentity;
	}

	public Map<String, String> getMatchAttributes() {
		return matchAttributes;
	}

	public void setMatchAttributes(Map<String, String> matchAttributes) {
		this.matchAttributes = matchAttributes;
	}
	
	public boolean matches(Map<String, String> inAttr) {
		return inAttr.equals(matchAttributes);
	}
	
}
