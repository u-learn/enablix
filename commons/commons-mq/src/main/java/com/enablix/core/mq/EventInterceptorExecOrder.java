package com.enablix.core.mq;

public enum EventInterceptorExecOrder {

	PROCESS_CTX_HEADER_ENRICHER(10.0f),
	PROCESS_CTX_INIT_ORDER(50.0f),
	DEFAULT(100.0f),
	EVENT_ERROR_PERSISTOR_ORDER(900.0f),
	PROCESS_CTX_DESTROY_ORDER(1000.0f);
	
	private float order;
	
	private EventInterceptorExecOrder(float order) {
		this.order = order;
	}

	public float getOrder() {
		return order;
	}

}
