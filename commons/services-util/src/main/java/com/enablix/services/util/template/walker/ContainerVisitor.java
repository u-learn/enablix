package com.enablix.services.util.template.walker;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public interface ContainerVisitor {

	void visit(ContainerType container);
	
}