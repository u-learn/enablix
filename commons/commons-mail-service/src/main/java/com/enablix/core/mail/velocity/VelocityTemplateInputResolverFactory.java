package com.enablix.core.mail.velocity;

import java.util.Collection;

public interface VelocityTemplateInputResolverFactory {

	<I> Collection<VelocityTemplateInputResolver<I>> getResolvers(I velocityTemplateInput);
	
}
