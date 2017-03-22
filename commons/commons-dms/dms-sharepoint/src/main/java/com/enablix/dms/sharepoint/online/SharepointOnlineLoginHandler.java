package com.enablix.dms.sharepoint.online;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.config.Configuration;
import com.enablix.dms.sharepoint.SharepointConstants;
import com.enablix.dms.sharepoint.SharepointException;
import com.enablix.dms.sharepoint.service.SharepointLoginHandler;
import com.enablix.dms.sharepoint.service.SharepointSession;

@Component
public class SharepointOnlineLoginHandler implements SharepointLoginHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SharepointOnlineLoginHandler.class);
	
	@Autowired
	private SharepointSecurityTokenResolver securityTokenResolver;
	
	@Autowired
	private SharepointOnlineSessionBuilder sessionBuilder;
	
	@Override
	public SharepointSession login(Configuration sharepointConfig) throws SharepointException {
		
		String securityToken = null;
		
		try {
			
			securityToken = securityTokenResolver.getSecurityToken(sharepointConfig);
			
		} catch (TransformerFactoryConfigurationError | SharepointException | URISyntaxException
				| TransformerException e) {
			
			LOGGER.error("Error getting security token", e);
			throw new SharepointException("Error getting security token", e);
		}
		
		String siteUrl = sharepointConfig.getStringValue(SharepointConstants.CFG_SP_SITE_URL_KEY);
		String baseFolder = sharepointConfig.getStringValue(SharepointConstants.CFG_BASE_FOLDER_KEY);
		
		SharepointOnlineSession spSession;
		
		try {
			
			spSession = sessionBuilder.createSession(securityToken, siteUrl, baseFolder);
			
		} catch (TransformerException | URISyntaxException | IOException e) {
			LOGGER.error("Error creating sharepoint session", e);
			throw new SharepointException("Error creating sharepoint session", e);
		}
		
		return spSession;
	}

	@Override
	public String loginType() {
		return SharepointConstants.LOGIN_TYPE_SP_ONLINE;
	}

}
