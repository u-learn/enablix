package com.enablix.core.mail.velocity;

public interface VelocityTemplateInputResolver<I> {

	void work(I input);
	
	boolean canHandle(Object velocityTemplateInput);
	
}
