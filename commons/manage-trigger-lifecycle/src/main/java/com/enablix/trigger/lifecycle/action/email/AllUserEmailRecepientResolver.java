package com.enablix.trigger.lifecycle.action.email;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.EmailAllUsersType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DatastoreUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class AllUserEmailRecepientResolver implements EmailRecipientResolver<EmailAllUsersType> {

	@Autowired
	private ContentCrudService crud;
	
	@Override
	public Set<ContentDataRef> resolveRecepientEmails(ContentDataRef triggerEntity, 
			ContentTemplate template, EmailAllUsersType allEmailUserDef) {
		
		Set<ContentDataRef> users = new HashSet<>();
		
		String userContainerQId = TemplateUtil.getUserContainerQId(template);
		String userCollName = DatastoreUtil.getCollectionName(template.getId(), userContainerQId);
		
		List<Map<String, Object>> allUserRecords = crud.findAllRecord(userCollName);
		
		for (Map<String, Object> userRecord : allUserRecords) {
			users.add(ContentDataUtil.contentDataToRef(userRecord, template, userContainerQId));
		}
		
		return users;
	}

	@Override
	public boolean canHandle(BaseEmailRecipientType recipientDef) {
		return recipientDef instanceof EmailAllUsersType;
	}
	
}
