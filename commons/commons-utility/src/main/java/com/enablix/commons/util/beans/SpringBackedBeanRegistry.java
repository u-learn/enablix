package com.enablix.commons.util.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public abstract class SpringBackedBeanRegistry<T> implements BeanPostProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) 
			throws BeansException {
		
		if (lookupForType().isAssignableFrom(bean.getClass())) {
			registerBean((T) bean);
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	protected abstract Class<T> lookupForType();

	protected abstract void registerBean(T bean);
}
