package com.enablix.app.mail.generic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@SuppressWarnings("rawtypes")
@Component
public class GenericEmailVelocityInputBuilderFactoryImpl extends SpringBackedBeanRegistry<GenericEmailVelocityInputBuilder> implements GenericEmailVelocityInputBuilderFactory {

	private Map<String, GenericEmailVelocityInputBuilder> builders = new HashMap<>();
	
	@Override
	public GenericEmailVelocityInputBuilder<?> getBuilder(String mailType) {
		return builders.get(mailType);
	}

	@Override
	protected Class<GenericEmailVelocityInputBuilder> lookupForType() {
		return GenericEmailVelocityInputBuilder.class;
	}

	@Override
	protected void registerBean(GenericEmailVelocityInputBuilder bean) {
		builders.put(bean.mailType(), bean);
	}

}
