package com.enablix.app.content.bounded;

import java.util.Collection;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.data.view.DataView;

public interface BoundedListBuilder {

	Collection<DataItem> buildBoundedList(
			TemplateFacade template, BoundedType boundedTypeDef, DataView view);
	
	boolean canHandle(BoundedType boundedType);
	
}
