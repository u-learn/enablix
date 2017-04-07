package com.enablix.trigger.lifecycle.action.email;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.collection.CollectionUtil.CollectionCreator;
import com.enablix.commons.util.collection.CollectionUtil.ITransformer;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.core.commons.xsdtopojo.EmailAllUsersType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.security.auth.repo.UserProfileRepository;

@Component
public class AllUserEmailRecepientResolver implements EmailRecipientResolver<EmailAllUsersType> {

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Override
	public Set<String> resolveRecepientUsers(ContentDataRef triggerEntity, 
			TemplateFacade template, EmailAllUsersType allEmailUserDef) {
		
		Set<String> userIdentities = CollectionUtil.transform(userProfileRepo.findAll(), 
			new CollectionCreator<Set<String>, String>() {
	
				@Override
				public Set<String> create() {
					return new HashSet<String>();
				}
				
			}, new ITransformer<UserProfile, String>() {
	
				@Override
				public String transform(UserProfile in) {
					return in.getIdentity();
				}
				
			});
		
		return userIdentities;
	}

	@Override
	public boolean canHandle(BaseEmailRecipientType recipientDef) {
		return recipientDef instanceof EmailAllUsersType;
	}
	
}
