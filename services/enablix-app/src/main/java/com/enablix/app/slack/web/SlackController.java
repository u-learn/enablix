package com.enablix.app.slack.web;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.slackdtls.SlackAccessToken;
import com.enablix.slack.integration.entities.SlackChannels;
import com.enablix.slack.integration.services.SlackServiceImpl;


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
			LOGGER.error(" Error Authorizing Slack Account ",e);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/sendMessage")
	public boolean sendMessage(@RequestParam("channelsSelectd") List<String> channelIDs,
			@RequestParam("containerQId") String containerQId,
			@RequestParam("contentIdentity") String contentIdentity,
			@RequestParam("slackCustomContent") String slackCustomContent) {
		try	{
			return slackServiceImpl.postMessageToChannel(ProcessContext.get().getUserId()
					, channelIDs, containerQId,contentIdentity,slackCustomContent);
		}
		catch(Exception e) {
			LOGGER.error(" Error Posting message to Slack ",e);
			return false;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/unauthSlack")
	public boolean unauthorizeSlack() {
		try	{
			return slackServiceImpl.unauthorize(ProcessContext.get().getUserId());
		}
		catch(Exception e) {
			LOGGER.error(" Error Unauthorizing Slack Account ",e);
			return false;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getChannelsLst")
	public SlackChannels getChannelsLst() {
		try	{
			return slackServiceImpl.getChannelDtls(ProcessContext.get().getUserId());
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Channel Details ",e);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getStoredSlackToken")
	public SlackAccessToken getStoredSlackDtls() {
		try	{
			return slackServiceImpl.getStoredSlackTeamDtls(ProcessContext.get().getUserId());
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Details ",e);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getSlackAppDtls", produces = "application/json")
	public HashMap<String, String> getSlackAppDtls() {
		try	{
			HashMap<String, String> slackAppDtls = new HashMap<String, String>();
			String clientId = slackServiceImpl.getClientId();
			String redirectDomain = slackServiceImpl.getRedirectDomain();
			slackAppDtls.put(AppConstants.SLACK_APP_CLIENT_ID, clientId);
			slackAppDtls.put(AppConstants.SLACK_APP_REDIRECT_DOMAIN, redirectDomain);
			return slackAppDtls;
		}
		catch(Exception e) {
			LOGGER.error("Error Retrieving Slack Client Id ",e);
			return null;
		}
	}
	
}
