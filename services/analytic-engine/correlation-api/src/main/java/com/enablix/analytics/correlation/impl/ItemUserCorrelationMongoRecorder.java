package com.enablix.analytics.correlation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.data.repo.ItemUserCorrelationRepository;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.TagsType;
import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.services.util.TagUtil;

@Component
public class ItemUserCorrelationMongoRecorder implements ItemUserCorrelationRecorder {

	@Autowired
	private ItemUserCorrelationRepository itemUserCorrelationRepo;
	
	@Override
	public ItemUserCorrelation recordItemUserCorrelation(ContentDataRef item, 
			ContentDataRef user, ItemUserCorrelationRuleType rule, TagsType tags) {
		
		ItemUserCorrelation itemUserCorr = new ItemUserCorrelation();
		itemUserCorr.setItem(item);
		itemUserCorr.setUser(user);
		itemUserCorr.setTags(TagUtil.createTags(tags.getTag()));
		
		itemUserCorr = itemUserCorrelationRepo.save(itemUserCorr);
		
		return itemUserCorr;
	}

}
