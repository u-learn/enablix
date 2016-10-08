package com.enablix.app.content.bounded;

import java.util.Collection;

public interface BoundedListManager {

	Collection<DataItem> getBoundedList(String templateId, String contentQId);
	
}
