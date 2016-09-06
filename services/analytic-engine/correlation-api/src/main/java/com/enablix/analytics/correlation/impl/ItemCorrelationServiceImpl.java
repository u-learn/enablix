package com.enablix.analytics.correlation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemItemCorrelator;
import com.enablix.analytics.correlation.ItemItemCorrelatorRegistry;
import com.enablix.analytics.correlation.data.dao.ItemItemCorrelationDao;
import com.enablix.app.content.ContentDataManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.correlation.ItemItemCorrelation;

@Component
public class ItemCorrelationServiceImpl implements ItemCorrelationService {

	@Autowired
	private ItemItemCorrelatorRegistry itemCorrelatorRegistry;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private ItemItemCorrelationDao dao;
	
	@Override
	public void correlateItems(ContentDataRef item) {
		ContentTemplate template = templateManager.getTemplate(item.getTemplateId());
		for (ItemItemCorrelator correlator : itemCorrelatorRegistry.getCorrelators()) {
			correlator.correlateItem(template, item);
		}
	}

	@Override
	public List<ContentDataRecord> fetchCorrelatedEntityRecords(ContentTemplate template, 
			ContentDataRef item, List<String> relatedItemQIds, List<String> tags) {
		
		List<ItemItemCorrelation> itemCorrs = 
				dao.findByItemAndRelatedItemQIdAndContainingTags(item, relatedItemQIds, tags);
		
		Map<String, List<String>> contentIdentitiesMap = new HashMap<String, List<String>>();
		for (ItemItemCorrelation corr: itemCorrs) {
			String relatedItemQId = corr.getRelatedItem().getContainerQId();
			List<String> identities = contentIdentitiesMap.get(relatedItemQId);
			if (identities == null) {
				identities = new ArrayList<>();
				contentIdentitiesMap.put(relatedItemQId, identities);
			}
			identities.add(corr.getRelatedItem().getInstanceIdentity());
		}
		
		List<ContentDataRecord> correlatedDataRecords = new ArrayList<>();
		for (Map.Entry<String, List<String>> contentIdentities : contentIdentitiesMap.entrySet()) {
			
			String relatedItemQId = contentIdentities.getKey();
			List<Map<String, Object>> contentRecords = contentDataMgr.getContentRecords(
					relatedItemQId, contentIdentities.getValue(), template);
			
			for (Map<String, Object> contentRecord : contentRecords) {
				ContentDataRecord record = new ContentDataRecord(template.getId(), 
						relatedItemQId, contentRecord);
				correlatedDataRecords.add(record);
			}
		}
		
		return correlatedDataRecords;
	}

	@Override
	public void deleteCorrelationsForItem(ContentDataRef item) {
		for (ItemItemCorrelator correlator : itemCorrelatorRegistry.getCorrelators()) {
			correlator.deleteCorrelationForItem(item);
		}
	}

}
