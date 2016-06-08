package com.enablix.core.mq.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.core.mq.EventInterceptor;
import com.enablix.core.mq.EventInterceptorRegistry;

@Component
public class DefaultEventInterceptorRegistry extends SpringBackedBeanRegistry<EventInterceptor> implements EventInterceptorRegistry {

	private Set<EventInterceptor> interceptors = new TreeSet<>(new Comparator<EventInterceptor>() {

		@Override
		public int compare(EventInterceptor o1, EventInterceptor o2) {
			final float compare = o1.order().getOrder() - o2.order().getOrder();
			return compare == 0 ? 0 : compare < 0 ? -1 : 1;
		}
		
	});
	
	@Override
	public Collection<EventInterceptor> interceptors() {
		return interceptors;
	}

	@Override
	protected Class<EventInterceptor> lookupForType() {
		return EventInterceptor.class;
	}

	@Override
	protected void registerBean(EventInterceptor bean) {
		interceptors.add(bean);
	}

}
