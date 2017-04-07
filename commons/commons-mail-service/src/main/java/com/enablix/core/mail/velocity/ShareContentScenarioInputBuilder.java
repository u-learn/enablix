package com.enablix.core.mail.velocity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.mail.velocity.input.ShareContentVelocityInput;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;

@Component
public class ShareContentScenarioInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public ShareContentVelocityInput build(String newUserId, 
			DisplayableContent sharedContent, String emailCustomContent, DataView view) {
		
		ShareContentVelocityInput input = new ShareContentVelocityInput(newUserId, sharedContent,emailCustomContent);
		
		Collection<VelocityTemplateInputResolver<ShareContentVelocityInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input, view);
		}
		
		return input;
	}
	
}
