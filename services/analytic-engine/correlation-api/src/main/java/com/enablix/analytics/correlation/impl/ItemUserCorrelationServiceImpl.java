package com.enablix.analytics.correlation.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemUserCorrelationService;
import com.enablix.analytics.correlation.ItemUserCorrelator;
import com.enablix.analytics.correlation.ItemUserCorrelatorRegistry;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.Tag;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.correlation.ItemUserCorrelation;

@Component
public class ItemUserCorrelationServiceImpl implements ItemUserCorrelationService {

	@Autowired
	private ItemUserCorrelatorRegistry itemUserCorrelatorRegistry;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public void correlateUsers(ContentDataRef item) {
		ContentTemplate template = templateManager.getTemplate(item.getTemplateId());
		for (ItemUserCorrelator correlator : itemUserCorrelatorRegistry.getCorrelators()) {
			correlator.correlateUsers(item, template);	
		}
	}

	@Override
	public List<ItemUserCorrelation> findCorrelatedUsers(ContentDataRef item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemUserCorrelation> findCorrelatedUsers(ContentDataRef item, Tag tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
