package com.enablix.app.slack.service;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.enablix.app.slack.entities.SlackChannels;
import com.enablix.app.slack.entities.SlackTeamDtls;
import com.enablix.core.domain.slackdtls.SlackAccessToken;
import com.enablix.core.domain.slackdtls.SlackAppDtls;
import com.enablix.core.system.repo.SlackAccessTokenRepository;
import com.enablix.core.system.repo.SlackAppDtlsRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class SlackServiceImpl implements SlackService{
	RestTemplate restTemplate ;

	@Value("${slack.base.url}")
	String BASEURL;

	@Value("${slack.attachment.fallback.text}")
	String fallbackText;
	
	@Value("${slack.oauth.access.api}")
	String OAUTH_ACCESS_API ;

	@Value("${slack.oauth.revoke.api}")
	String OAUTH_REVOKE;

	@Value("${slack.channel.list}")
	String CHANNEL_LIST_API;


	@Value("${slack.channel.post.message}")
	String CHANNEL_POST_TEXTMSG;

	@Value("${slack.enablixapp.name}")
	private String appName;

	private static final Logger LOGGER = LoggerFactory.getLogger(SlackServiceImpl.class);

	@Autowired
	private SlackAppDtlsRepository slackDtlsRepo;

	@Autowired
	private SlackAccessTokenRepository slackTokenRepo;

	@Override
	public SlackAccessToken authorize(String _code,String userID) throws Exception {
		SlackAppDtls slackDtls = slackDtlsRepo.findByAppName(appName);
		restTemplate = new RestTemplate();
		URI targetUrl= UriComponentsBuilder.fromUriString(BASEURL)
				.path(OAUTH_ACCESS_API)
				.queryParam("client_id", slackDtls.getClientID())
				.queryParam("client_secret", slackDtls.getClientSecret())
				.queryParam("code",_code)
				.build()
				.toUri();
		SlackTeamDtls slackTeamDtls = restTemplate.getForObject(targetUrl,
				SlackTeamDtls.class);
		SlackAccessToken slackAccessToken = saveUserSpecificToken(slackTeamDtls, userID);
		return slackAccessToken;
	}

	@Override
	public SlackChannels getChannelDtls(String usrID)  {
		restTemplate = new RestTemplate();
		SlackAccessToken slackAccessToken = getStoredSlackTeamDtls(usrID) ;
		URI targetUrl= UriComponentsBuilder.fromUriString(BASEURL)
				.path(CHANNEL_LIST_API)
				.queryParam("token",slackAccessToken.getAccessToken())
				.queryParam("exclude_archived",true)
				.build()
				.toUri();
		SlackChannels channelDtls = restTemplate.getForObject(targetUrl,
				SlackChannels.class);
		return channelDtls;
	}

	@Override
	public boolean postMessageToChannel(String userID,String channelID, String portalURL,String contentName) {
		restTemplate = new RestTemplate();
		SlackAccessToken slackAccessToken = getStoredSlackTeamDtls(userID) ;
		URI targetUrl= UriComponentsBuilder.fromUriString(BASEURL)
				.path(CHANNEL_POST_TEXTMSG)
				.queryParam("token",slackAccessToken.getAccessToken())
				.queryParam("channel",channelID)
				.queryParam("text", portalURL)
				.build()
				.toUri();
		ObjectNode objNode = restTemplate.getForObject(targetUrl,
				ObjectNode.class);
		boolean resp = objNode.get("ok").asBoolean();
		if( resp ){
			slackTokenRepo.delete(slackAccessToken);
			return true;
		}
		return false;
	}

	@Override
	public SlackAccessToken saveUserSpecificToken(SlackTeamDtls slackTeamDtls, String userID) {
		try
		{
			SlackAccessToken slackAccessToken = new SlackAccessToken(userID
					,slackTeamDtls.getAccess_token(),slackTeamDtls.getTeam_name());
			slackTokenRepo.save(slackAccessToken);
			return slackAccessToken;
		}
		catch(Exception e)
		{
			LOGGER.error(" Error in saving User Specific Token", e);
			throw e;
		}
	}

	@Override
	public SlackAccessToken getStoredSlackTeamDtls(String userID) {
		SlackAccessToken slackAccessToken= slackTokenRepo.findByUserID(userID);
		return slackAccessToken;
	}

	@Override
	public boolean unauthorize(String userID) {
		restTemplate = new RestTemplate();

		SlackAccessToken slackAccessToken= slackTokenRepo.findByUserID(userID);
		URI targetUrl= UriComponentsBuilder.fromUriString(BASEURL)
				.path(OAUTH_REVOKE)
				.queryParam("token", slackAccessToken.getAccessToken())
				.build()
				.toUri();
		ObjectNode objNode = restTemplate.getForObject(targetUrl,
				ObjectNode.class);
		boolean resp = objNode.get("ok").asBoolean();
		if( resp ){
			slackTokenRepo.delete(slackAccessToken);
			return true;
		}
		return false;
	}
}
