package com.enablix.core.mq.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventHeaders;
import com.enablix.core.mq.EventInterceptorExecOrder;

@Component
public class ProcessContentInitInterceptor extends EventInterceptorSupport {

	@Override
	public EventInterceptorExecOrder order() {
		return EventInterceptorExecOrder.PROCESS_CTX_INIT_ORDER;
	}

	@Override
	public void beforeProcessing(Event<?> event) {
		
		String userId = (String) event.getHeaders().get(EventHeaders.USER_ID);
		String tenantId = (String) event.getHeaders().get(EventHeaders.TENANT_ID);
		String templateId = (String) event.getHeaders().get(EventHeaders.TEMPLATE_ID);
		
		ProcessContext.initialize(userId, tenantId, templateId);
	}
	
}
