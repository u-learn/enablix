package com.enablix.commons.util.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import com.enablix.commons.util.ExecutionOrderAware;
import com.enablix.commons.util.ExecutionOrderComparator;

public abstract class SpringBackedOrderedBeanRegistry<T extends ExecutionOrderAware> extends SpringBackedBeanRegistry<T> {

	private Set<T> beans = 
			new TreeSet<>(new ExecutionOrderComparator());
	
	@Override
	protected void registerBean(T bean) {
		beans.add(bean);
	}
	
	protected Collection<T> registeredInstances() {
		return Collections.unmodifiableCollection(beans);
	}

}
