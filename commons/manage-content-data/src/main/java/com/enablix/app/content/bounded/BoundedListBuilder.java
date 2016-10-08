package com.enablix.app.content.bounded;

import java.util.Collection;

import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface BoundedListBuilder {

	Collection<DataItem> buildBoundedList(ContentTemplate template, BoundedType boundedTypeDef);
	
	boolean canHandle(BoundedType boundedType);
	
}
