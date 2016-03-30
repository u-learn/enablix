package com.enablix.core.mail.velocity;

import java.util.List;

public interface VelocityTemplateInputResolverFactory {

	<I> List<VelocityTemplateInputResolver<I>> getResolvers(I velocityTemplateInput);
	
}
