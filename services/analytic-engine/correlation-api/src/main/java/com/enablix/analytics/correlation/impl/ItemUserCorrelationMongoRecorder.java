package com.enablix.analytics.correlation.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.data.repo.ItemUserCorrelationRepository;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.TagsType;
import com.enablix.core.correlation.CorrelationRuleSource;
import com.enablix.core.correlation.CorrelationSource;
import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.core.correlation.SourceMetadata.SourceType;
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
		
		CorrelationSource<CorrelationRuleSource> corrSrc = new CorrelationSource<>();
		CorrelationRuleSource srcMd = new CorrelationRuleSource(rule.getId(), SourceType.CORRELATION_RULE);
		corrSrc.setMetadata(srcMd);
		
		itemUserCorr.addSource(corrSrc);
		
		return saveCorrelation(itemUserCorr);
	}

	@Override
	public ItemUserCorrelation recordItemUserCorrelation(ContentDataRef item, ContentDataRef userRef, List<String> tags) {
		
		ItemUserCorrelation itemUserCorr = new ItemUserCorrelation();
		itemUserCorr.setItem(item);
		itemUserCorr.setUser(userRef);
		itemUserCorr.setTags(TagUtil.createTags(tags));
		
		return saveCorrelation(itemUserCorr);
	}
	
	private ItemUserCorrelation saveCorrelation(ItemUserCorrelation itemUserCorr) {
		
		ItemUserCorrelation existingCorr = itemUserCorrelationRepo.findByItemInstanceIdentityAndUserInstanceIdentity(
				itemUserCorr.getItem().getInstanceIdentity(), itemUserCorr.getUser().getInstanceIdentity());
		
		if (existingCorr != null) {
			existingCorr.getTags().addAll(itemUserCorr.getTags());
			itemUserCorr = existingCorr;itemUserCorrelationRepo.save(existingCorr);
		}
		
		itemUserCorr = itemUserCorrelationRepo.save(itemUserCorr);
		return itemUserCorr;
	}

	@Override
	public void removeItemUserCorrelation(ContentDataRef item) {
		itemUserCorrelationRepo.deleteByItemInstanceIdentity(item.getInstanceIdentity());
	}

}
