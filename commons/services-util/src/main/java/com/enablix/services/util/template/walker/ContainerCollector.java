package com.enablix.services.util.template.walker;

import java.util.ArrayList;
import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public class ContainerCollector implements ContainerVisitor {

	private List<ContainerType> containers;
	
	public ContainerCollector() {
		this.containers = new ArrayList<>();
	}
	
	@Override
	public void visit(ContainerType container) {
		containers.add(container);
	}

	public List<ContainerType> getContainers() {
		return containers;
	}

}
