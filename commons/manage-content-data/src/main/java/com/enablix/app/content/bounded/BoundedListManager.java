package com.enablix.app.content.bounded;

import java.util.Collection;

import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.data.view.DataView;

public interface BoundedListManager {

	Collection<DataItem> getBoundedList(String templateId, String contentQId, DataView userDataView);

	Collection<DataItem> getBoundedList(BoundedType boundedType, DataView userDataView);
	
}
