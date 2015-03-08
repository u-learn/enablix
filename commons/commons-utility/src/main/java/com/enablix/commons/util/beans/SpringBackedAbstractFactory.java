package com.enablix.commons.util.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class SpringBackedAbstractFactory<T> extends SpringBackedBeanRegistry<T> {

	private final Collection<T> matchedBeans = new ArrayList<T>();

	protected Collection<T> registeredInstances() {
		return Collections.unmodifiableCollection(matchedBeans);
	}

	protected void registerBean(T bean) {
		matchedBeans.add(bean);
	}
}
