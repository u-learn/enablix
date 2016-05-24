package com.enablix.analytics.correlation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.data.repo.ItemItemCorrelationRepository;
import com.enablix.core.api.ContentDataRef;
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
		
		ItemItemCorrelation itemCorr = new ItemItemCorrelation();
		itemCorr.setItem(item);
		itemCorr.setRelatedItem(relatedItem);
		itemCorr.setCorrelationScore(1f);
		itemCorr.setItemCorrelationRuleId(itemCorrRule.getId());
		itemCorr.setTags(TagUtil.createTags(tags.getTag()));
		
		itemCorr = itemItemCorrelationRepo.save(itemCorr);
		
		return itemCorr;
	}

}
