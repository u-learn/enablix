package com.enablix.app.content.bounded;

import java.util.Collection;

import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.services.util.template.TemplateWrapper;

public interface BoundedListBuilder {

	Collection<DataItem> buildBoundedList(TemplateWrapper template, BoundedType boundedTypeDef);
	
	boolean canHandle(BoundedType boundedType);
	
}
