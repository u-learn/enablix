package com.enablix.dms.sharepoint.service;

import com.enablix.core.domain.config.Configuration;
import com.enablix.dms.sharepoint.SharepointException;

public interface SharepointLoginHandler {

	SharepointSession login(Configuration sharepointConfig) throws SharepointException;
	
	String loginType();
	
}
