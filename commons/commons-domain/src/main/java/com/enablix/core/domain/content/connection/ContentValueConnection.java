package com.enablix.core.domain.content.connection;

import java.util.List;

public class ContentValueConnection {

	private Object contentValue;
	
	// List of qualifiedId of the connected containers
	private List<String> connectedContainers;

	public Object getContentValue() {
		return contentValue;
	}

	public void setContentValue(Object contentValue) {
		this.contentValue = contentValue;
	}

	public List<String> getConnectedContainers() {
		return connectedContainers;
	}

	public void setConnectedContainers(List<String> connectedContainers) {
		this.connectedContainers = connectedContainers;
	}
	
}
