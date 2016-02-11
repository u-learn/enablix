package com.enablix.app.content.event;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class ContentDataEventListenerRegistryImpl 
		extends SpringBackedAbstractFactory<ContentDataEventListener> 
		implements ContentDataEventListenerRegistry {

	@Override
	public Collection<ContentDataEventListener> getListeners() {
		return registeredInstances();
	}

	@Override
	protected Class<ContentDataEventListener> lookupForType() {
		return ContentDataEventListener.class;
	}

}
