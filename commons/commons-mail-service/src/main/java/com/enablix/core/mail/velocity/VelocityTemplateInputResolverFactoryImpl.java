package com.enablix.core.mail.velocity;

import java.util.Collection;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.ExecutionOrderComparator;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@SuppressWarnings("rawtypes")
@Component
public class VelocityTemplateInputResolverFactoryImpl extends SpringBackedAbstractFactory<VelocityTemplateInputResolver> 
		implements VelocityTemplateInputResolverFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <I> Collection<VelocityTemplateInputResolver<I>> getResolvers(I velocityTemplateInput) {
		
		Collection<VelocityTemplateInputResolver<I>> resolvers = 
				new TreeSet<>(new ExecutionOrderComparator()); 
		
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
