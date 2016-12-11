package com.enablix.analytics.correlation.context;

import java.util.ArrayList;
import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public class CorrelationContext {

	// QId of the containers which should be considered for correlation 
	// in the current correlation process
	private List<String> containersInScope;
	
	private ContentTemplate template;
	
	public CorrelationContext() {
		this.containersInScope = new ArrayList<>();
	}
	
	public List<String> getContainersInScope() {
		return containersInScope;
	}

	public void setContainersInScope(List<String> containersInScope) {
		this.containersInScope = containersInScope;
	}

	public ContentTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ContentTemplate template) {
		this.template = template;
	}

	public boolean shouldCorrelateContainer(String containerQId) {
		return containersInScope.isEmpty() || containersInScope.contains(containerQId);
	}
	
}
