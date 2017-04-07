package com.enablix.trigger.lifecycle.action.email;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.data.dao.ItemUserCorrelationDao;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.core.commons.xsdtopojo.EmailCorrelatedUsersType;
import com.enablix.core.commons.xsdtopojo.EmailUsersType;
import com.enablix.core.commons.xsdtopojo.FilterTagsType;
import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class CorrelatedUsersEmailRecepientResolver implements EmailRecipientResolver<EmailCorrelatedUsersType> {

	@Autowired
	private ItemUserCorrelationDao itemUserCorrelationDao;
	
	@Override
	public Set<String> resolveRecepientUsers(ContentDataRef triggerEntity, 
			TemplateFacade template, EmailCorrelatedUsersType emailCorrUsersDef) {
		
		Set<String> users = new HashSet<>();
		
		for (EmailUsersType usersDef : emailCorrUsersDef.getUsers()) {
			
			FilterTagsType filterTags = usersDef.getFilterTags();
			
			List<String> tags = new ArrayList<>();
			if (filterTags != null) {
				tags = filterTags.getTag();
			} 
			
			List<ItemUserCorrelation> itemUserCorrs = 
					itemUserCorrelationDao.findByItemAndContainingTags(triggerEntity, tags, MongoDataView.ALL_DATA);
			
			if (itemUserCorrs != null) {
				for (ItemUserCorrelation itemUserCorr : itemUserCorrs) {
					users.add(itemUserCorr.getUserProfileIdentity());
				}
			}
			
		}
		
		return users;
	}

	@Override
	public boolean canHandle(BaseEmailRecipientType recipientDef) {
		return recipientDef instanceof EmailCorrelatedUsersType;
	}
	
}
