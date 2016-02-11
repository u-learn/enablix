package com.enablix.app.content.event;

import java.util.Collection;

public interface ContentDataEventListenerRegistry {

	Collection<ContentDataEventListener> getListeners();
	
}
