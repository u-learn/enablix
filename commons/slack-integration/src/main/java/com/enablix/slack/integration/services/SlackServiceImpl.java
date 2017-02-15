package com.enablix.slack.integration.services;

import java.net.URI;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.config.ConfigurationProviderChain;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.activity.UserAccountActivity;
import com.enablix.core.domain.activity.UserAccountActivity.AccountActivityType;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.domain.slackdtls.SlackAccessToken;
import com.enablix.core.system.repo.SlackAccessTokenRepository;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.template.TemplateWrapper;
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

	@Value("${slack.channel.list}")
	private String CHANNEL_LIST_API;

	@Value("${slack.channel.post.message}")
	private String CHANNEL_POST_TEXTMSG;

	@Value("${slack.attachment.footer.icon}")
	private String FOOTER_ICON;

	@Value("${slack.attachment.color}")
	private String COLOR;

	@Value("${slack.enablixapp.name}")
	private String appName;

	@Value("${slack.attachment.footer.label}")
	private String FOOTER_TEXT;

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
	
	private RestTemplate restTemplate;

	public SlackServiceImpl() {
		restTemplate = new RestTemplate();
	}

	public SlackAccessToken authorize(String _code,String userID) throws Exception {
		
		Configuration config = configProvider.getConfiguration(AppConstants.SLACK_APP);
		String clientId = config.getStringValue(AppConstants.SLACK_APP_CLIENT_ID);
		String clientSecret =  config.getStringValue(AppConstants.SLACK_APP_CLIENT_SECRET);
		
		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(OAUTH_ACCESS_API)
				.queryParam("client_id", clientId)
				.queryParam("client_secret", clientSecret)
				.queryParam("code",_code)
				.build()
				.toUri();
		
		SlackTeamDtls slackTeamDtls = restTemplate.getForObject(targetUrl, SlackTeamDtls.class);
		
		if( slackTeamDtls!=null  && slackTeamDtls.getAccessToken() != null && !slackTeamDtls.getAccessToken().isEmpty()) {
			
			SlackAccessToken slackAccessToken = saveUserSpecificToken(slackTeamDtls, userID);
			auditUserActivity(AccountActivityType.SLACK_AUTH);
			return slackAccessToken;
			
		} else {
			throw new Exception("Access Token is not present in the response");
		}
	}
	
	private void auditUserActivity(AccountActivityType activityType){
	
		ActivityAudit slackActivity = new ActivityAudit();
		
		UserAccountActivity slackUserAcc = new UserAccountActivity(activityType);
		slackActivity.setActivity(slackUserAcc);
		slackActivity.setActivityTime(Calendar.getInstance().getTime());
		slackActivity.setChannel(new ActivityChannel(Channel.WEB));
		
		ActivityLogger.auditActivity(slackActivity); 
	}
	
	public SlackChannels getChannelDtls(String usrID)  {
	
		SlackAccessToken slackAccessToken = getStoredSlackTeamDtls(usrID) ;
		
		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(CHANNEL_LIST_API)
				.queryParam("token",slackAccessToken.getAccessToken())
				.queryParam("exclude_archived",true)
				.build()
				.toUri();
		
		SlackChannels channelDtls = restTemplate.getForObject(targetUrl, SlackChannels.class);
		
		return channelDtls;
	}

	public boolean postMessageToChannel(String userID, String channelID,
			String containerQId, String contentIdentity, String slackCustomContent) 
					throws JsonProcessingException {

		String templateId = ProcessContext.get().getTemplateId();
		TemplateWrapper template = templateMgr.getTemplateWrapper(templateId);

		Map<String, Object> record = contentDataMgr.getContentRecord(
				ContentDataRef.createContentRef(templateId, containerQId, 
						contentIdentity, null), template);

		ContentDataRecord dataRecord = new ContentDataRecord(templateId, containerQId, record);

		DisplayContext ctx = new DisplayContext();

		DisplayableContent displayableContent = contentBuilder.build(template, dataRecord, ctx);


		String slackAttachments = AttachmentDecorator.getDecoratedAttachment(displayableContent,
				FALL_BACK_TEXT, FOOTER_ICON,COLOR,FOOTER_TEXT);
		SlackAccessToken slackAccessToken = getStoredSlackTeamDtls(userID) ;

		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(CHANNEL_POST_TEXTMSG)
				.queryParam("token",slackAccessToken.getAccessToken())
				.queryParam("channel",channelID)
				.queryParam("text", slackCustomContent)
				.queryParam("attachments", slackAttachments)
				.build()
				.toUri();
		
		ObjectNode objNode = restTemplate.getForObject(targetUrl, ObjectNode.class);
		
		boolean resp = objNode.get("ok").asBoolean();
		
		if (resp) {
			String sharingId = IdentityUtil.generateIdentity(this);
			ActivityLogger.auditContentShare(templateId, displayableContent, channelID,
					ShareMedium.WEB, Channel.SLACK, sharingId, displayableContent.getTitle());
			return true;
		}
		
		return false;
	}

	public SlackAccessToken saveUserSpecificToken(SlackTeamDtls slackTeamDtls, String userID) throws Exception {
		
		try	{
			
			SlackAccessToken slackAccessToken = new SlackAccessToken(
					userID, slackTeamDtls.getAccessToken(), slackTeamDtls.getTeamName());
			
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
		
		URI targetUrl= UriComponentsBuilder.fromUriString(BASE_URL)
				.path(OAUTH_REVOKE)
				.queryParam("token", slackAccessToken.getAccessToken())
				.build()
				.toUri();
		
		ObjectNode objNode = restTemplate.getForObject(targetUrl, ObjectNode.class);
		
		boolean resp = objNode.get("ok").asBoolean();
		if (resp) {
			slackTokenRepo.delete(slackAccessToken);
			auditUserActivity(AccountActivityType.SLACK_UNAUTH);
			return true;
		}
		
		return false;
	}
}
