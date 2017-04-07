package com.enablix.core.mail.velocity;

import com.enablix.commons.util.ExecutionOrderAware;
import com.enablix.data.view.DataView;

public interface VelocityTemplateInputResolver<I> extends ExecutionOrderAware {

	void work(I input, DataView view);
	
	boolean canHandle(Object velocityTemplateInput);
	
}
