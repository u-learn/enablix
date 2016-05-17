package com.enablix.core.mail.velocity;

import com.enablix.commons.util.ExecutionOrderAware;

public interface VelocityTemplateInputResolver<I> extends ExecutionOrderAware {

	void work(I input);
	
	boolean canHandle(Object velocityTemplateInput);
	
}
