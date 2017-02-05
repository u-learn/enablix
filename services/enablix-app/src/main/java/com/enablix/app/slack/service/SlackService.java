package com.enablix.app.slack.service;

import com.enablix.app.slack.entities.SlackChannels;
import com.enablix.app.slack.entities.SlackTeamDtls;
import com.enablix.core.domain.slackdtls.SlackAccessToken;

public interface SlackService {
	SlackAccessToken authorize(String _code,String userID) throws Exception;
	boolean unauthorize(String userID);
	SlackChannels getChannelDtls(String usrID);
	boolean postMessageToChannel(String userID,String channelID, String portalURL,String contentName);
	SlackAccessToken saveUserSpecificToken(SlackTeamDtls slackTeamDtls,String userID);
	SlackAccessToken getStoredSlackTeamDtls(String userID);
}