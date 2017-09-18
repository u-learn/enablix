package com.enablix.core.mq.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.enablix.core.mq.EventSubscription;
import com.enablix.core.mq.util.EventUtil;

public class BeanEventHandlerRegisterer implements BeanPostProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanEventHandlerRegisterer.class);
	
	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		
		// Check whether if the bean methods contain @EventSubscription
		// annotation. If so, register in eventbus.

		final Class targetClass = AopProxyUtils.ultimateTargetClass(bean);
		this.checkAndRegister(bean, targetClass);
		return bean;
	}

	/**
	 * recursively goes up the class hierarchy to look for methods
	 * with @EventSubscription
	 *
	 * @param obj
	 *            the instance of listener.
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	private void checkAndRegister(final Object obj, final Class clazz) {
		
		if (null != clazz && !clazz.equals(Object.class)) {
			
			final Method[] methods = clazz.getDeclaredMethods();
			
			if (methods != null) {
				
				for (final Method m : methods) {
					
					final EventSubscription es = m.getAnnotation(EventSubscription.class);
					
					if (es != null) {
						
						m.setAccessible(true);
						final String[] eventNames = es.eventName();
						
						for (final String eventName : eventNames) {
							
							final String handlerId = es.handlerId() != null ? es.handlerId() : m.getName();
							final DefaultEventListener listener = this.newListener(handlerId, obj, m);
							
							LOGGER.debug("Registering handler method {} for Event {}", clazz.getName() + "." + m.getName(),
									eventName);
							
							EventUtil.register(eventName, listener);
							
							LOGGER.debug("Registered handler method {} for Event {}", clazz.getName() + "." + m.getName(),
									eventName);
						}
					}
				}
			}
			
			this.checkAndRegister(obj, clazz.getSuperclass());
		}
	}

	private DefaultEventListener newListener(final String handlerId, final Object obj, final Method m) {
		if (obj instanceof Proxy) {
			return new ProxySupportEventListener(handlerId, (Proxy) obj, m);
		}
		return new DefaultEventListener(handlerId, obj, m);
	}
}
