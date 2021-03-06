package com.enablix.slack.integration.services;

import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.config.ConfigurationProviderChain;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.activity.UserAccountActivity;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.domain.slackdtls.SlackAccessToken;
import com.enablix.core.system.repo.SlackAccessTokenRepository;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.DataViewUtil;
import com.enablix.slack.integration.entities.SlackChannels;
import com.enablix.slack.integration.entities.SlackTeamDtls;
import com.enablix.slack.integration.utils.AttachmentDecorator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class SlackServiceImpl implements SlackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlackServiceImpl.class);

	@Value("${slack.base.url}")
	private String BASE_URL;

	@Value("${slack.attachment.fallback.text}")
	private String FALL_BACK_TEXT;

	@Value("${slack.oauth.access.api}")
	private String OAUTH_ACCESS_API ;

	@Value("${slack.oauth.revoke.api}")
	private String OAUTH_REVOKE;

	@Value("${slack.auth.test}")
	private String OAUTH_TEST;
	
	@Value("${slack.channel.list}")
	private String CHANNEL_LIST_API;

	@Value("${slack.channel.post.message}")
	private String CHANNEL_POST_TEXTMSG;

	@Value("${slack.attachment.footer.icon}")
	private String FOOTER_ICON;

	@Value("${slack.attachment.color}")
	private String COLOR;

	@Value("${slack.attachment.footer.label}")
	private String FOOTER_TEXT;
	
	@Value("${slack.app.enablix.clientId}")
	private String clientId;
	
	@Value("${slack.app.enablix.clientSecret}")
	private String clientSecret;
	
	@Value("${slack.app.enablix.redirect.domain}")
	private String redirectDomain;

	@Autowired
	private SlackAccessTokenRepository slackTokenRepo;

	@Autowired
	private ContentDataManager contentDataMgr;

	@Autowired
	private TemplateManager templateMgr;

	@Autowired
	private DisplayableContentBuilder contentBuilder;

	@Autowired
	private ConfigurationProviderChain configProvider;

	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	private RestTemplate restTemplate;

	public SlackServiceImpl() {
		restTemplate = new RestTemplate();
	}

	public SlackAccessToken authorize(String _code, String userID, String redirectUri) throws Exception {

		String redirectURI = getRedirectURI(redirectUri);
		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(OAUTH_ACCESS_API)
				.queryParam(AppConstants.SLACK_REQUEST_CLIENT_ID, clientId)
				.queryParam(AppConstants.SLACK_REQUEST_CLIENT_SEC, clientSecret)
				.queryParam(AppConstants.SLACK_REQUEST_CODE,_code)
				.queryParam(AppConstants.SLACK_REQUEST_REDIRECT_URI,redirectURI)
				.build()
				.toUri();

		SlackTeamDtls slackTeamDtls = restTemplate.getForObject(targetUrl, SlackTeamDtls.class);

		if( slackTeamDtls!=null  && slackTeamDtls.getAccessToken() != null && !slackTeamDtls.getAccessToken().isEmpty()) {

			SlackAccessToken slackAccessToken = saveUserSpecificToken(slackTeamDtls, userID, redirectURI);
			auditUserActivity(ActivityType.SLACK_AUTH);
			return slackAccessToken;

		} else {
			throw new Exception("Access Token is not present in the response");
		}
	}

	private String getRedirectURI(String redirectUri) {
		return StringUtil.hasText(redirectUri) ? redirectUri : (redirectDomain + "/app.html");
	}

	private void auditUserActivity(ActivityType activityType){

		ActivityAudit slackActivity = new ActivityAudit();

		UserAccountActivity slackUserAcc = new UserAccountActivity(activityType);
		slackActivity.setActivity(slackUserAcc);
		slackActivity.setActivityTime(Calendar.getInstance().getTime());
		slackActivity.setChannel(new ActivityChannel(Channel.WEB));

		ActivityLogger.auditActivity(slackActivity); 
	}

	public SlackChannels getChannelDtls(String usrID) throws Exception  {

		SlackAccessToken slackAccessToken = getStoredSlackTeamDtls(usrID) ;
		String redirectURI = getRedirectURI(slackAccessToken.getRedirectUri());
		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(CHANNEL_LIST_API)
				.queryParam(AppConstants.SLACK_REQUEST_TOKEN,slackAccessToken.getAccessToken())
				.queryParam(AppConstants.SLACK_REQUEST_ARCHIVED,true)
				.queryParam(AppConstants.SLACK_REQUEST_REDIRECT_URI,redirectURI)
				.build()
				.toUri();

		SlackChannels channelDtls = restTemplate.getForObject(targetUrl, SlackChannels.class);
		if(channelDtls==null || channelDtls.getSlackChannels()==null || channelDtls.getSlackChannels().size()==0){
			throw new Exception(" Channel List is Empty ");
		}
		return channelDtls;
	}

	public boolean postMessageToChannel(String userID, List<String> channelIDs,
			String containerQId, String contentIdentity, String slackCustomContent) 
					throws JsonProcessingException {
		boolean resp = false;
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);

		Map<String, Object> record = contentDataMgr.getContentRecord(
				ContentDataRef.createContentRef(templateId, containerQId, 
						contentIdentity, null), template, DataViewUtil.allDataView());

		ContentDataRecord dataRecord = new ContentDataRecord(templateId, containerQId, record);

		DisplayContext ctx = new DisplayContext();

		DisplayableContent displayableContent = contentBuilder.build(template, dataRecord, ctx);

		docUrlPopulator.populateUnsecureUrl(displayableContent, userID, ctx);
		
		String slackAttachments = AttachmentDecorator.getDecoratedAttachment(displayableContent,
				FALL_BACK_TEXT, FOOTER_ICON,COLOR,FOOTER_TEXT);
		SlackAccessToken slackAccessToken = getStoredSlackTeamDtls(userID) ;

		String redirectURI = getRedirectURI(slackAccessToken.getRedirectUri());
		String targetURI = BASE_URL + CHANNEL_POST_TEXTMSG;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(AppConstants.SLACK_REQUEST_TOKEN, slackAccessToken.getAccessToken());
		map.add(AppConstants.SLACK_REQUEST_TEXT, slackCustomContent);
		map.add(AppConstants.SLACK_REQUEST_ATTACHMENT, slackAttachments);
		map.add(AppConstants.SLACK_REQUEST_REDIRECT_URI, redirectURI);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		for(String channelId : channelIDs) {
			
			map.add(AppConstants.SLACK_REQUEST_CHANNEL, channelId);

			ResponseEntity<ObjectNode> response = restTemplate.postForEntity(targetURI, request , ObjectNode.class);
			ObjectNode objNode = response.getBody();

			resp = objNode.get(AppConstants.SLACK_RESPONSE_OK).asBoolean();

			if (resp) {
				String sharingId = IdentityUtil.generateIdentity(this);
				ActivityLogger.auditContentShare(templateId, displayableContent, channelId,
						ShareMedium.WEB, Channel.SLACK, sharingId, displayableContent.getTitle());
			} else {
				LOGGER.error("Error sharing on slack: {}", objNode);
				return false;
			}
		}
		return resp;
	}

	public SlackAccessToken saveUserSpecificToken(SlackTeamDtls slackTeamDtls, String userID, String redirectUri) throws Exception {

		try	{

			String redirectURI = getRedirectURI(redirectUri);
			URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
					.path(OAUTH_TEST)
					.queryParam(AppConstants.SLACK_REQUEST_TOKEN,slackTeamDtls.getAccessToken())
					.queryParam(AppConstants.SLACK_REQUEST_REDIRECT_URI,redirectURI)
					.build()
					.toUri();

			ObjectNode objNode = restTemplate.getForObject(targetUrl, ObjectNode.class);
			String slackUserID = objNode.get(AppConstants.SLACK_RESPONSE_USER).asText();
			
			SlackAccessToken slackAccessToken = new SlackAccessToken(
					userID, slackTeamDtls.getAccessToken(), slackTeamDtls.getTeamName(),
					slackUserID, redirectURI);

			slackTokenRepo.save(slackAccessToken);
			return slackAccessToken;

		} catch(Exception e)	{
			LOGGER.error(" Error in saving User Specific Token", e);
			throw e;
		}
	}

	public SlackAccessToken getStoredSlackTeamDtls(String userID) {
		SlackAccessToken slackAccessToken = slackTokenRepo.findByUserID(userID);
		return slackAccessToken;
	}

	public boolean unauthorize(String userID) {

		SlackAccessToken slackAccessToken= slackTokenRepo.findByUserID(userID);

		String redirectURI = getRedirectURI(slackAccessToken.getRedirectUri());
		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(OAUTH_REVOKE)
				.queryParam(AppConstants.SLACK_REQUEST_TOKEN, slackAccessToken.getAccessToken())
				.queryParam(AppConstants.SLACK_REQUEST_REDIRECT_URI, redirectURI)
				.build()
				.toUri();

		ObjectNode objNode = restTemplate.getForObject(targetUrl, ObjectNode.class);

		boolean resp = objNode.get(AppConstants.SLACK_RESPONSE_OK).asBoolean();
		
		if (resp) {
			deleteToken(slackAccessToken);
			return true;
		}
		else{
			if (objNode.get(AppConstants.SLACK_ERROR).asText()
					.equalsIgnoreCase(AppConstants.SLACK_ERROR_TOKEN_REVOKED)) {
				deleteToken(slackAccessToken);
				return true;
			}
		}
		return false;
	}
	
	private void deleteToken(SlackAccessToken slackAccessToken){
		slackTokenRepo.delete(slackAccessToken);
		auditUserActivity(ActivityType.SLACK_UNAUTH);
	}

	@Override
	public String getRedirectDomain() {
		return redirectDomain;
	}

	@Override
	public String getClientId() {
		Configuration config = configProvider.getConfiguration(AppConstants.SLACK_APP);
		String clientId = config.getStringValue(AppConstants.SLACK_APP_CLIENT_ID);
		LOGGER.info(" Returning the Client Id from the properties file :: "+clientId);
		return clientId;
	}
}
