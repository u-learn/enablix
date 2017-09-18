package com.enablix.services.util.template.walker;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface ContentItemVisitor {

	void visit(ContainerType container, ContentItemType item);
	
}
