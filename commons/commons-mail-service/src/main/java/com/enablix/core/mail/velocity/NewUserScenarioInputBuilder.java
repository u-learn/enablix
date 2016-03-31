package com.enablix.core.mail.velocity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.mail.velocity.input.UserWelcomeVelocityInput;

@Component
public class NewUserScenarioInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public UserWelcomeVelocityInput build(String newUserId) {
		
		UserWelcomeVelocityInput input = new UserWelcomeVelocityInput(newUserId);
		
		List<VelocityTemplateInputResolver<UserWelcomeVelocityInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input);
		}
		
		return input;
	}
	
}
