package com.enablix.analytics.correlation.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemItemCorrelator;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.correlation.ItemItemCorrelation;

@Component
public class ItemCorrelationServiceImpl implements ItemCorrelationService {

	@Autowired
	private ItemItemCorrelator itemCorrelator;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public void correlateItems(ContentDataRef item) {
		ContentTemplate template = templateManager.getTemplate(item.getTemplateId());
		itemCorrelator.correlateItem(template, item);
	}

	@Override
	public List<ItemItemCorrelation> fetchItemItemCorrelations(ContentDataRef item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemItemCorrelation> fetchItemItemCorrelations(ContentDataRef item, List<String> relatedItemQIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
