package com.enablix.app.content.bounded;

import java.util.Collection;

import com.enablix.core.commons.xsdtopojo.BoundedType;

public interface BoundedListManager {

	Collection<DataItem> getBoundedList(String templateId, String contentQId);

	Collection<DataItem> getBoundedList(BoundedType boundedType);
	
}
