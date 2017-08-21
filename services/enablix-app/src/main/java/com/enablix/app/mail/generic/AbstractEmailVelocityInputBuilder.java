package com.enablix.app.mail.generic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.data.view.DataView;

public abstract class AbstractEmailVelocityInputBuilder<T extends BasicEmailVelocityInput> implements GenericEmailVelocityInputBuilder<T> {

	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void processInputForRecipient(Recipient recipient, T input, DataView dataView) {
		
		input.setRecipientUserId(recipient.getEmailId());

		Collection<VelocityTemplateInputResolver<T>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input, dataView);
		}
		
	}

	@Override
	public T build(EmailRequest request, DataView dataView) {
		
		T input = createInputInstance();
		input.setInputData(request.getInputData());
		
		input = buildInput(input, request, dataView);
		
		return input;
	}
	
	protected abstract T buildInput(T input, EmailRequest request, DataView dataView);
	
	protected abstract T createInputInstance();

}
