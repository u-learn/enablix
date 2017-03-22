package com.enablix.dms.sharepoint.service;

public interface SharepointLoginHandlerFactory {

	SharepointLoginHandler getLoginHandler(String loginType);
	
}
