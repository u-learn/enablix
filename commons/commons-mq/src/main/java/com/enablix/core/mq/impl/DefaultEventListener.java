package com.enablix.core.mq.impl;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.core.mq.EventListener;

public class DefaultEventListener implements EventListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventListener.class);

	private final String handlerId;
	private final Object targetHandlerObj;
	private final Method targetMethod;

	public DefaultEventListener(final String handlerId, final Object targetHandlerObj, final Method targetMethod) {
		
		this.targetHandlerObj = targetHandlerObj;
		this.targetMethod = targetMethod;
		this.handlerId = handlerId.equalsIgnoreCase("DEFAULT") ? targetHandlerObj.toString() : handlerId;
		this.targetMethod.setAccessible(true);
		
		LOGGER.trace("Initialized EventListener. HandlerType: {}, targetMethod {}", 
				targetHandlerObj.getClass(), targetMethod.toString());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof DefaultEventListener) {
			final DefaultEventListener that = (DefaultEventListener) obj;
			return handlerId.equals(that.handlerId);
		}
		return false;
	}

	@Override
	public void handle(final Object eventPayload) throws Throwable {
		
		LOGGER.debug("Delegating event procesing to HandlerType: {}, targetMethod {} for payload {}", 
				targetHandlerObj.getClass(), targetMethod.toString(), eventPayload);
		try {
			
			targetMethod.invoke(targetHandlerObj, eventPayload);
			
		} catch (final Exception e) {
			
			LOGGER.error("Error in delegating event procesing to HandlerType: {}, targetMethod {}", 
					targetHandlerObj.getClass(), targetMethod.toString(), e);
			throw e;
		}
	}

	@Override
	public String handlerId() {
		return handlerId;
	}

	@Override
	public int hashCode() {
		return handlerId.hashCode();
	}

	public Object targetHandlerObj() {
		return targetHandlerObj;
	}

	public Method targetMethod() {
		return targetMethod;
	}

	protected Class<? extends Object> getTargetHandlerType() {
		return targetHandlerObj.getClass();
	}

}
