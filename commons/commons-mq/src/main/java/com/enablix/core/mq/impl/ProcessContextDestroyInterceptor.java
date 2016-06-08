package com.enablix.core.mq.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventInterceptorExecOrder;

@Component
public class ProcessContextDestroyInterceptor extends EventInterceptorSupport {

	@Override
	public EventInterceptorExecOrder order() {
		return EventInterceptorExecOrder.PROCESS_CTX_DESTROY_ORDER;
	}

	@Override
	public void onProcessingComplete(Event<?> event) {
		ProcessContext.clear();
	}

}
