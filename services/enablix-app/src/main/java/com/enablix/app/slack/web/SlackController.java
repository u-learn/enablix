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
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.slackdtls.SlackAccessToken;


@RestController
@RequestMapping("slack")
public class SlackController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlackController.class);
	
	@Autowired
	SlackServiceImpl slackServiceImpl;
	
	@RequestMapping(method = RequestMethod.POST, value = "/authorizeSlack")
	public SlackAccessToken authorizeSlack(@RequestParam("code") String code) {
		try{
			return slackServiceImpl.authorize(code, ProcessContext.get().getUserId());
		}
		catch(Exception e){
			LOGGER.error("Error Retrieving Slack Dtls ",e);
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sendMessage")
	public boolean sendMessage(@RequestParam("channelID") String channelID,
			@RequestParam("containerQId") String containerQId,
			@RequestParam("contentIdentity") String contentIdentity,
			@RequestParam("slackCustomContent") String slackCustomContent) {
		try	{
			return slackServiceImpl.postMessageToChannel(ProcessContext.get().getUserId()
					, channelID, containerQId,contentIdentity,slackCustomContent);
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Dtls ",e);
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/unauthSlack")
	public boolean unauthorizeSlack() {
		try	{
			return slackServiceImpl.unauthorize(ProcessContext.get().getUserId());
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Dtls ",e);
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getChannelsLst")
	public SlackChannels getChannelsLst() {
		try	{
			return slackServiceImpl.getChannelDtls(ProcessContext.get().getUserId());
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Channel Dtls ",e);
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getStoredSlackToken")
	public SlackAccessToken getStoredSlackDtls() {
		try	{
			return slackServiceImpl.getStoredSlackTeamDtls(ProcessContext.get().getUserId());
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Dtls ",e);
			return null;
		}
	}
}