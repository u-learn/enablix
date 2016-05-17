package com.enablix.core.mail.velocity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;

@Component
public class WeeklyDigestScenarioInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public WeeklyDigestVelocityInput build(String tenantId) {
		
		WeeklyDigestVelocityInput input = new WeeklyDigestVelocityInput();
		
		Collection<VelocityTemplateInputResolver<WeeklyDigestVelocityInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input);
		}
		
		return input;
	}
	
}
