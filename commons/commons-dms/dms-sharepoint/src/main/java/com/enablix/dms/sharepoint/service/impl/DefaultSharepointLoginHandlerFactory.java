package com.enablix.dms.sharepoint.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.dms.sharepoint.service.SharepointLoginHandler;
import com.enablix.dms.sharepoint.service.SharepointLoginHandlerFactory;

@Component
public class DefaultSharepointLoginHandlerFactory extends SpringBackedBeanRegistry<SharepointLoginHandler> implements SharepointLoginHandlerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSharepointLoginHandlerFactory.class);
	
	private Map<String, SharepointLoginHandler> loginHandlers = new HashMap<>();
	
	@Override
	public SharepointLoginHandler getLoginHandler(String loginType) {
		
		SharepointLoginHandler loginHandler = loginHandlers.get(loginType);
		
		if (loginHandler == null) {
			LOGGER.error("No sharepoint login handler found for type [{}]", loginType);
			throw new UnsupportedOperationException("Unsupported login type [" + loginType + "]");
		}
		
		return loginHandler;
	}

	@Override
	protected Class<SharepointLoginHandler> lookupForType() {
		return SharepointLoginHandler.class;
	}

	@Override
	protected void registerBean(SharepointLoginHandler bean) {
		loginHandlers.put(bean.loginType(), bean);
	}

}
