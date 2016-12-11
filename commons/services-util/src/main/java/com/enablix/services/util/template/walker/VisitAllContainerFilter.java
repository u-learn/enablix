package com.enablix.services.util.template.walker;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public class VisitAllContainerFilter implements ContainerFilter {

	@Override
	public boolean accept(ContainerType container) {
		return true;
	}
	
}
