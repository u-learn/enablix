package com.enablix.core.mail.velocity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.format.DisplayableContent;
import com.enablix.core.mail.velocity.input.ShareContentVelocityInput;

@Component
public class ShareContentScenarioInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public ShareContentVelocityInput build(String newUserId, DisplayableContent sharedContent) {
		
		ShareContentVelocityInput input = new ShareContentVelocityInput(newUserId, sharedContent);
		
		Collection<VelocityTemplateInputResolver<ShareContentVelocityInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input);
		}
		
		return input;
	}
	
}
