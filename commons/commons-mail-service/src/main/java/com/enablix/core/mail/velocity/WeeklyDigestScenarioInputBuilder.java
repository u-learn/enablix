package com.enablix.core.mail.velocity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;
import com.enablix.data.view.DataView;

@Component
public class WeeklyDigestScenarioInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public WeeklyDigestVelocityInput build(DataView view) {
		
		WeeklyDigestVelocityInput input = new WeeklyDigestVelocityInput();
		String inputIdentity = IdentityUtil.generateIdentity(input);
		input.setIdentity(inputIdentity);
		
		Collection<VelocityTemplateInputResolver<WeeklyDigestVelocityInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input, view);
		}
		
		return input;
	}
	
}
