package com.enablix.analytics.correlation.context;

import java.util.HashSet;
import java.util.Set;

import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;

public class CorrelationContext {

	// QId of the containers which should be considered for correlation 
	// in the current correlation process
	private Set<String> containersInScope;
	
	private Set<ContainerBusinessCategoryType> filteredContainerCategories;
	
	private TemplateWrapper template;
	
	public CorrelationContext() {
		this.containersInScope = new HashSet<>();
		this.filteredContainerCategories = new HashSet<>();
	}
	
	public Set<String> getContainersInScope() {
		return containersInScope;
	}

	public void setContainersInScope(Set<String> containersInScope) {
		this.containersInScope = containersInScope;
	}

	public Set<ContainerBusinessCategoryType> getFilteredContainerCategories() {
		return filteredContainerCategories;
	}

	public void setFilteredContainerCategories(Set<ContainerBusinessCategoryType> filteredContainerCategories) {
		this.filteredContainerCategories = filteredContainerCategories;
	}

	public TemplateWrapper getTemplate() {
		return template;
	}

	public void setTemplate(TemplateWrapper template) {
		this.template = template;
	}

	public boolean shouldCorrelateContainer(String containerQId) {
		
		if (containersInScope.isEmpty() || containersInScope.contains(containerQId)) {
			
			return true;
			
		} else {
			
			// check the business category of the container. If the business category of the container
			// is not in the list of filtered business categories, then we allow correlation
			ContainerBusinessCategoryType businessCategory = 
					TemplateUtil.getContainerBusinessCategory(getTemplate().getTemplate(), containerQId);
			
			return businessCategory == null || !filteredContainerCategories.contains(businessCategory); 
		}
		
	}
	
}
