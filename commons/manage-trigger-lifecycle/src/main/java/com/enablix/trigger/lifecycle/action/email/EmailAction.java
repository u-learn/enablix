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

import com.enablix.app.content.ui.format.DisplayableContent;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.CorrelatedEntitiesType;
import com.enablix.core.commons.xsdtopojo.EmailActionType;
import com.enablix.core.commons.xsdtopojo.EmailContentTriggerEntityType;
import com.enablix.core.commons.xsdtopojo.EmailRecepientType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.services.util.TemplateUtil;
import com.enablix.trigger.lifecycle.action.CheckpointAction;

@Component
public class EmailAction implements CheckpointAction<ContentChange, EmailActionType> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailAction.class);
	
	@Autowired
	private EmailContentResolverFactory contentResolverFactory;
	
	@Autowired
	private CorrelatedUsersEmailRecepientResolver recepientUserResolver;
	
	@Autowired
	private ContentCrudService contentCrudService;
	
	@Autowired
	private VelocityTemplateInputResolverFactory velocityInputFactory;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private DisplayableContentBuilder displayableContentBuilder;
	
	@Override
	public boolean canHandle(ActionType action) {
		return action instanceof EmailActionType;
	}

	@Override
	public void run(LifecycleCheckpoint<ContentChange> checkpoint, ContentTemplate template, EmailActionType actionType) {
		
		// map of content identity to content record, keeping a map helps in removing the duplicates
		// of the same record returned from different content resolvers
		Map<String, ContentDataRecord> emailContent = new HashMap<>();
		
		EmailContentTriggerEntityType triggerEntityContent = actionType.getEmailContent().getTriggerEntity();
		
		if (triggerEntityContent != null) {
			fetchEmailContent(checkpoint, template, emailContent, triggerEntityContent);
		}
		
		CorrelatedEntitiesType correlatedEntities = actionType.getEmailContent().getCorrelatedEntities();
		
		if (correlatedEntities != null) {
			fetchEmailContent(checkpoint, template, emailContent, correlatedEntities);
		}
		
		// find out the recepients
		EmailRecepientType recepientDef = actionType.getRecepient();
		Set<ContentDataRef> recepientUsers = recepientUserResolver.resolveRecepientEmails(
				checkpoint.getTrigger().getTriggerItem(), template, recepientDef.getCorrelatedUsers());
		
		if (CollectionUtil.isNotEmpty(recepientUsers)) {
			
			List<String> userIdentities = new ArrayList<>();
			for (ContentDataRef userRef : recepientUsers) {
				userIdentities.add(userRef.getInstanceIdentity());
			}
			
			String userContainerQId = TemplateUtil.getUserContainerQId(template);
			String userContainerCollName = TemplateUtil.resolveCollectionName(template, userContainerQId);
			
			SearchFilter usersIdentitiesInFilter = new StringListFilter(
					ContentDataConstants.IDENTITY_KEY, userIdentities, ConditionOperator.IN);
			
			List<Map<String, Object>> userRecords = contentCrudService.findAllRecordForCriteria(
					userContainerCollName, usersIdentitiesInFilter.toPredicate(new Criteria()));
			
			if (userRecords != null) {
				Set<String> recepientEmailIds = extractEmailIdsFromUsers(userRecords, template);
				
				// send email
				LOGGER.debug("Sending mail to: {}", recepientEmailIds);
				
				for (String emailId : recepientEmailIds) {
					sendEmail(template, emailId, actionType.getEmailTemplate().getBody().getTemplateName(), 
							actionType.getEmailTemplate().getBody().getTemplateName(), emailContent.values());
				}
			}
		}
		
	}
	
	private void sendEmail(ContentTemplate template, String recepientEmailId, String bodyTemplateName, 
			String subjectTemplateName, Collection<ContentDataRecord> collection) {

		List<DisplayableContent> displayableEmailContent = createDisplayableContent(template, collection);
		
		LOGGER.debug("Email content: {}", displayableEmailContent);
		
		TriggerEmailVelocityInput velocityIn = new TriggerEmailVelocityInput();
		velocityIn.setEmailContent(displayableEmailContent);
		
		Collection<VelocityTemplateInputResolver<TriggerEmailVelocityInput>> resolvers = 
				velocityInputFactory.getResolvers(velocityIn);
		
		for (VelocityTemplateInputResolver<TriggerEmailVelocityInput> resolver : resolvers) {
			resolver.work(velocityIn);
		}
		
		mailService.sendHtmlEmail(velocityIn, recepientEmailId, "TRIGGER_CHECKPOINT", bodyTemplateName, subjectTemplateName);
		
	}

	private List<DisplayableContent> createDisplayableContent(ContentTemplate template, Collection<ContentDataRecord> collection) {
		
		List<DisplayableContent> displayableContentList = new ArrayList<>();
		
		for (ContentDataRecord rec : collection) {
			DisplayableContent displayContent = displayableContentBuilder.build(template, rec);
			displayableContentList.add(displayContent);
		}
		
		return displayableContentList;
	}
	
	private Set<String> extractEmailIdsFromUsers(List<Map<String, Object>> userRecords, ContentTemplate template) {
		
		Set<String> emailIds = new HashSet<>();
		
		String emailAttrId = TemplateUtil.getUserContainerEmailAttrId(template);
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
	
	private void fetchEmailContent(LifecycleCheckpoint<ContentChange> checkpoint, ContentTemplate template,
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

}
