package com.enablix.core.mq.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxySupportEventListener extends DefaultEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxySupportEventListener.class);

	public ProxySupportEventListener(String handlerId, Proxy proxy, Method targetMethod) {
		super(handlerId, proxy, targetMethod);
	}

	@Override
	public void handle(final Object eventPayload) throws Throwable {
		
		LOGGER.debug("Delegating event procesing to proxy HandlerType: {}, targetMethod {}", 
				targetHandlerObj().getClass(), targetMethod().toString());
		
		try {
		
			final Proxy aopProxy = getProxy();
			
			InvocationHandler invocationHandler = null;
			if (aopProxy instanceof InvocationHandler) {
				invocationHandler = (InvocationHandler) aopProxy;
			} else {
				invocationHandler = Proxy.getInvocationHandler(aopProxy);
			}
			
			final Object[] arg = new Object[] { eventPayload };
			invocationHandler.invoke(aopProxy, targetMethod(), arg);
			
		} catch (final Throwable e) {
			LOGGER.error("Error in delegating event procesing to HandlerType: {}, targetMethod {}", 
					targetHandlerObj().getClass(), targetMethod().toString(), e);
			throw e;
		}
	}

	public Proxy getProxy() {
		return (Proxy) targetHandlerObj();
	}
	
}
