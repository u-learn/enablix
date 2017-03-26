package com.enablix.trigger.lifecycle.action.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;
import com.enablix.core.commons.xsdtopojo.CorrelatedEntitiesType;
import com.enablix.core.commons.xsdtopojo.EmailActionType;
import com.enablix.core.commons.xsdtopojo.EmailContentTriggerEntityType;
import com.enablix.core.commons.xsdtopojo.EmailRecipientType;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.ContentChange.TriggerType;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.core.system.repo.UserRepository;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;
import com.enablix.trigger.lifecycle.action.CheckpointAction;

@Component
public class EmailAction implements CheckpointAction<ContentChange, EmailActionType> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailAction.class);
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private EmailContentResolverFactory contentResolverFactory;
	
	@Autowired
	private EmailRecipientHelper recepientUserHelper;
	
	@Autowired
	private ContentCrudService contentCrudService;
	
	@Autowired
	private VelocityTemplateInputResolverFactory velocityInputFactory;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private DisplayableContentBuilder displayableContentBuilder;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Autowired
	private TextLinkProcessor textLinkProcessor;
	

	
	@Override
	public boolean canHandle(ActionType action) {
		return action instanceof EmailActionType;
	}

	@Override
	public void run(LifecycleCheckpoint<ContentChange> checkpoint, TemplateWrapper template, EmailActionType actionType) {
		
		// map of content identity to content record, keeping a map helps in removing the duplicates
		// of the same record returned from different content resolvers
		Map<String, ContentDataRecord> emailContent = new HashMap<>();
		
		ContentDataRef triggerItemRef = checkpoint.getTrigger().getTriggerItem();
		EmailContentTriggerEntityType triggerEntityContent = actionType.getEmailContent().getTriggerEntity();
		
		if (triggerEntityContent != null) {
			fetchEmailContent(checkpoint, template, emailContent, triggerEntityContent);
		}
		
		CorrelatedEntitiesType correlatedEntities = actionType.getEmailContent().getCorrelatedEntities();
		
		if (correlatedEntities != null) {
			fetchEmailContent(checkpoint, template, emailContent, correlatedEntities);
		}

		if (!emailContent.isEmpty()) {
			// find out the recipients
			EmailRecipientType recepientDef = actionType.getRecipient();
			Set<ContentDataRef> recepientUsers = recepientUserHelper.getEmailRecipients(triggerItemRef, template, recepientDef);
			
			if (CollectionUtil.isNotEmpty(recepientUsers)) {
				
				List<String> userIdentities = new ArrayList<>();
				for (ContentDataRef userRef : recepientUsers) {
					userIdentities.add(userRef.getInstanceIdentity());
				}
				
				String userContainerQId = TemplateUtil.getUserContainerQId(template.getTemplate());
				String userContainerCollName = template.getCollectionName(userContainerQId);
				
				SearchFilter usersIdentitiesInFilter = new StringListFilter(
						ContentDataConstants.IDENTITY_KEY, userIdentities, ConditionOperator.IN);
				
				List<Map<String, Object>> userRecords = contentCrudService.findAllRecordForCriteria(
						userContainerCollName, usersIdentitiesInFilter.toPredicate(new Criteria()));
				
				if (userRecords != null) {
					Set<String> recepientEmailIds = extractEmailIdsFromUsers(userRecords, template);
					
					sendEmails(template, actionType, checkpoint.getTrigger().getType(), 
							emailContent, triggerItemRef, recepientEmailIds);
				}
			}
		}
	}

	private void sendEmails(TemplateWrapper template, EmailActionType actionType, TriggerType triggerType,
			Map<String, ContentDataRecord> emailContent, ContentDataRef triggerItemRef, Set<String> recepientEmailIds) {
		
		TriggerEmailVelocityInput velocityIn = new TriggerEmailVelocityInput();
		
		// find trigger entity data
		ContentDataRecord entityDataRec = emailContent.get(triggerItemRef.getInstanceIdentity());
		
		if (entityDataRec == null) {
			Map<String, Object> entityRec = contentDataMgr.getContentRecord(triggerItemRef, template);
			entityDataRec = new ContentDataRecord(triggerItemRef.getTemplateId(), triggerItemRef.getContainerQId(), entityRec);
		}
		
		DisplayContext ctx = new DisplayContext();
		
		DisplayableContent triggerEntityDispRecord = displayableContentBuilder.build(template, entityDataRec, ctx);
		if (triggerEntityDispRecord != null) {
			velocityIn.setTriggerEntity(triggerEntityDispRecord);
		}
		
		// send email
		LOGGER.debug("Sending mail to: {}", recepientEmailIds);
		
		List<DisplayableContent> displayableEmailContent = createDisplayableContent(template, emailContent.values(), ctx);
		
		LOGGER.debug("Email content: {}", displayableEmailContent);
		
		velocityIn.setEmailContent(displayableEmailContent);
		velocityIn.setTriggerType(triggerType);
		
		String triggerEntityLabel = "";
		if (entityDataRec != null) {
			triggerEntityLabel = ContentDataUtil.findStudioLabelValue(
					entityDataRec.getRecord(), template, entityDataRec.getContainerQId());
			triggerEntityLabel = triggerEntityLabel == null ? "" : triggerEntityLabel;
		}
		
		velocityIn.setTriggerEntityTitle(triggerEntityLabel);
		
		Collection<VelocityTemplateInputResolver<TriggerEmailVelocityInput>> resolvers = 
				velocityInputFactory.getResolvers(velocityIn);
		
		for (VelocityTemplateInputResolver<TriggerEmailVelocityInput> resolver : resolvers) {
			resolver.work(velocityIn);
		}
		
		for (String emailId : recepientEmailIds) {
			
			for (DisplayableContent displayableContent : velocityIn.getEmailContent()) {
				docUrlPopulator.populateUnsecureUrl(displayableContent, emailId);
				textLinkProcessor.process(displayableContent, template, emailId);
			}
			
			velocityIn.setRecipientUserId(emailId);
			
			UserProfile recipientUser = userProfileRepo.findByEmail(emailId.toLowerCase());
			velocityIn.setRecipientUser(recipientUser);
			
			mailService.sendHtmlEmail(velocityIn, emailId, "TRIGGER_CHECKPOINT", 
					actionType.getEmailTemplate().getBody().getTemplateName(), 
					actionType.getEmailTemplate().getSubject().getTemplateName());
			
			auditContentShare(template.getTemplate().getId(), velocityIn, emailId);
		}
	}
	
	private List<DisplayableContent> createDisplayableContent(TemplateWrapper template, 
			Collection<ContentDataRecord> collection, DisplayContext ctx) {
		
		List<DisplayableContent> displayableContentList = new ArrayList<>();
		
		for (ContentDataRecord rec : collection) {
			DisplayableContent displayContent = displayableContentBuilder.build(template, rec, ctx);
			displayableContentList.add(displayContent);
		}
		
		return displayableContentList;
	}
	
	private Set<String> extractEmailIdsFromUsers(List<Map<String, Object>> userRecords, TemplateWrapper template) {
		
		Set<String> emailIds = new HashSet<>();
		
		String emailAttrId = TemplateUtil.getUserContainerEmailAttrId(template.getTemplate());
		if (!StringUtil.isEmpty(emailAttrId)) {
			
			for (Map<String, Object> user : userRecords) {
			
				String userEmailId = (String) user.get(emailAttrId);
				if (userEmailId != null) {
					emailIds.add(userEmailId);
				}
			}
		}
		
		return emailIds;
	}
	
	private void fetchEmailContent(LifecycleCheckpoint<ContentChange> checkpoint, TemplateWrapper template,
			Map<String, ContentDataRecord> emailContent, BaseEmailContentType triggerEntityContent) {
		
		EmailContentResolver<BaseEmailContentType> resolver = 
				contentResolverFactory.getResolver(triggerEntityContent);
		
		List<ContentDataRecord> triggerEntity = resolver.getEmailContent(
				triggerEntityContent, template, checkpoint.getTrigger().getTriggerItem());
		
		addContentRecordsToMap(triggerEntity, emailContent);
	}
	
	private void addContentRecordsToMap(List<ContentDataRecord> triggerEntity, 
			Map<String, ContentDataRecord> emailContent) {
		
		for (ContentDataRecord rec : triggerEntity) {
			String recIdentity = (String) rec.getRecord().get(ContentDataConstants.IDENTITY_KEY);
			emailContent.put(recIdentity, rec);
		}
		
	}
	
	private void auditContentShare(String templateId, TriggerEmailVelocityInput sharedContent, 
			String sharedWithEmailId) {
		
		List<ContentDataRef> sharedContentList = new ArrayList<>();
		
		Collection<DisplayableContent> emailContent = sharedContent.getEmailContent();
		if (emailContent != null) {
			for (DisplayableContent content : emailContent) {
				sharedContentList.add(ContentDataRef.createContentRef(templateId, 
						content.getContainerQId(), content.getRecordIdentity(),
						content.getTitle()));
			}
		}
		
		ActivityLogger.auditContentShare(templateId, sharedContentList, 
				ShareMedium.CORRELATION, Channel.EMAIL, sharedContent.getIdentity(), 
				sharedWithEmailId, null);
	}

}
