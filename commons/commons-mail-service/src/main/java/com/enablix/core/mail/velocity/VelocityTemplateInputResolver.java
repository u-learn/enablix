package com.enablix.core.mail.velocity;

public interface VelocityTemplateInputResolver<I> {

	void work(I velocityTemplateInput);
	
	boolean canHandle(Object velocityTemplateInput);
	
}
