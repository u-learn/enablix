package com.enablix.app.slack.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.slack.entities.SlackChannels;
import com.enablix.app.slack.service.SlackServiceImpl;
import com.enablix.core.domain.slackdtls.SlackAccessToken;


@RestController
@RequestMapping("slack")
public class SlackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SlackController.class);
	
	
	@Autowired
	SlackServiceImpl slackServiceImpl;
	
	@RequestMapping(method = RequestMethod.POST, value = "/authorizeSlack")
	public SlackAccessToken authorizeSlack(@RequestParam("code")String _code,@RequestParam("userID")String userID) {
		try
		{
			
			return slackServiceImpl.authorize(_code,userID);
		}
		catch(Exception e)
		{
			LOGGER.error("Error Retrieving Slack Dtls ");
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sendMessage")
	public boolean sendMessage(@RequestParam("userID")String userID,@RequestParam("channelID")String channelID,
			@RequestParam("containerQId")String containerQId,@RequestParam("contentIdentity")String contentIdentity,
			@RequestParam("contentName")String contentName,@RequestParam("portalURL")String portalURL) {
		try
		{
			return slackServiceImpl.postMessageToChannel(userID, channelID, portalURL,contentName);
		}
		catch(Exception e)
		{
			LOGGER.error("Error Retrieving Slack Dtls ");
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/unauthSlack")
	public boolean unauthorizeSlack(@RequestParam("userID")String userID) {
		try
		{
			
			return slackServiceImpl.unauthorize(userID);
		}
		catch(Exception e)
		{
			LOGGER.error("Error Retrieving Slack Dtls ");
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getChannelsLst")
	public SlackChannels getChannelsLst(@RequestParam("userID")String userID) {
		try
		{
			return slackServiceImpl.getChannelDtls(userID);
		}
		catch(Exception e)
		{
			LOGGER.error("Error Retrieving Slack Channel Dtls ");
			return null;
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/getStoredSlackToken")
	public SlackAccessToken getStoredSlackDtls(@RequestParam("userID")String userID) {
		try
		{
			
			return slackServiceImpl.getStoredSlackTeamDtls(userID);
		}
		catch(Exception e)
		{
			LOGGER.error("Error Retrieving Slack Dtls ");
			return null;
		}
	}
}