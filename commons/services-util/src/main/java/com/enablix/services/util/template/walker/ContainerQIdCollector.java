package com.enablix.services.util.template.walker;

import java.util.ArrayList;
import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public class ContainerQIdCollector implements ContainerVisitor {

	private List<String> containerQIds;
	
	public ContainerQIdCollector() {
		this.containerQIds = new ArrayList<>();
	}
	
	@Override
	public void visit(ContainerType container) {
		containerQIds.add(container.getQualifiedId());
	}

	public List<String> getContainerQIds() {
		return containerQIds;
	}
	
}
