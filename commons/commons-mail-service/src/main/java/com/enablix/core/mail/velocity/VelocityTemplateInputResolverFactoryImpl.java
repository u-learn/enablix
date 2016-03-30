package com.enablix.core.mail.velocity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@SuppressWarnings("rawtypes")
@Component
public class VelocityTemplateInputResolverFactoryImpl extends SpringBackedAbstractFactory<VelocityTemplateInputResolver> 
		implements VelocityTemplateInputResolverFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <I> List<VelocityTemplateInputResolver<I>> getResolvers(I velocityTemplateInput) {
		List<VelocityTemplateInputResolver<I>> resolvers = new ArrayList<>(); 
		for (VelocityTemplateInputResolver<I> resolver : registeredInstances()) {
			if (resolver.canHandle(velocityTemplateInput)) {
				resolvers.add(resolver);
			}
		}
		return resolvers;
	}

	@Override
	protected Class<VelocityTemplateInputResolver> lookupForType() {
		return VelocityTemplateInputResolver.class;
	}

}
