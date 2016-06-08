package com.enablix.core.mq;

public interface EventListener {

	String handlerId();
	
	void handle(Object payload) throws Throwable;
	
}
