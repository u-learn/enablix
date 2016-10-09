package com.enablix.analytics.correlation.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.data.repo.ItemItemCorrelationRepository;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.Tag;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.TagsType;
import com.enablix.core.correlation.ItemItemCorrelation;
import com.enablix.services.util.TagUtil;

@Component
public class ItemItemCorrelationMongoRecorder implements ItemItemCorrelationRecorder {

	@Autowired
	private ItemItemCorrelationRepository itemItemCorrelationRepo;
	
	@Override
	public ItemItemCorrelation recordItemCorrelation(ContentDataRef item, ContentDataRef relatedItem,
			ItemCorrelationRuleType itemCorrRule, TagsType tags) {
		
		ItemItemCorrelation itemCorr = 
				itemItemCorrelationRepo.findByItemInstanceIdentityAndRelatedItemInstanceIdentity(
							item.getInstanceIdentity(), relatedItem.getInstanceIdentity());
		
		Set<Tag> corrTags = TagUtil.createTags(tags.getTag());
		
		if (itemCorr == null) {
		
			itemCorr = new ItemItemCorrelation();
			itemCorr.setItem(item);
			itemCorr.setRelatedItem(relatedItem);
			itemCorr.setCorrelationScore(1f);
			itemCorr.setItemCorrelationRuleId(itemCorrRule.getId());
			itemCorr.setTags(corrTags);
			
		} else {
			
			Set<Tag> existingTags = itemCorr.getTags();
			
			if (existingTags == null) {
				itemCorr.setTags(corrTags);
			} else {
				existingTags.addAll(corrTags);
			}
		}
		
		itemCorr = itemItemCorrelationRepo.save(itemCorr);
		
		return itemCorr;
	}

	@Override
	public void removeItemCorrelations(ContentDataRef item) {
		itemItemCorrelationRepo.deleteByItemInstanceIdentity(item.getInstanceIdentity());
	}

	@Override
	public void removeItemCorrelationsByRule(ContentDataRef item, ItemCorrelationRuleType rule) {
		itemItemCorrelationRepo.deleteByItemInstanceIdentityAndItemCorrelationRuleId(item.getInstanceIdentity(), rule.getId());
	}

}
