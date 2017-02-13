package com.enablix.slack.integration.services;

import com.enablix.core.domain.slackdtls.SlackAccessToken;
import com.enablix.slack.integration.entities.SlackChannels;
import com.enablix.slack.integration.entities.SlackTeamDtls;

public interface SlackService {
	
	SlackAccessToken authorize(String code, String userID) throws Exception;
	
	boolean unauthorize(String userID);
	
	SlackChannels getChannelDtls(String usrID) throws Exception;
	
	boolean postMessageToChannel(String userID, String channelID, String containerQId, String contentIdentity, String slackCustomContent) throws Exception;
	
	SlackAccessToken saveUserSpecificToken(SlackTeamDtls slackTeamDtls, String userID) throws Exception;
	
	SlackAccessToken getStoredSlackTeamDtls(String userID);
	
}
