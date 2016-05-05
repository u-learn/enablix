package com.enablix.core.mail.velocity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.mail.velocity.input.UserWelcomeVelocityInput;
import com.enablix.core.mail.velocity.input.ShareContentVelocityInput;

@Component
public class ShareContentScenarioInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public ShareContentVelocityInput build(String newUserId, Object sharedContent) {
		
		ShareContentVelocityInput input = new ShareContentVelocityInput(newUserId,sharedContent);
		
		List<VelocityTemplateInputResolver<ShareContentVelocityInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input);
		}
		
		return input;
	}
	
}
