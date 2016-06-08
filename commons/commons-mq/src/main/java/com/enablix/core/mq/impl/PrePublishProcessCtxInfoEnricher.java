package com.enablix.core.mq.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventHeaders;
import com.enablix.core.mq.EventInterceptorExecOrder;

@Component
public class PrePublishProcessCtxInfoEnricher extends EventInterceptorSupport {

	@Override
	public EventInterceptorExecOrder order() {
		return EventInterceptorExecOrder.PROCESS_CTX_HEADER_ENRICHER;
	}

	@Override
	public void beforePublishing(Event<?> event) {
		ProcessContext processCtx = ProcessContext.get();
		event.addHeader(EventHeaders.USER_ID, processCtx.getUserId());
		event.addHeader(EventHeaders.TEMPLATE_ID, processCtx.getTemplateId());
		event.addHeader(EventHeaders.TENANT_ID, processCtx.getTenantId());
	}
	
}
