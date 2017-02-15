package com.enablix.trigger.lifecycle.action.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.data.dao.ItemItemCorrelationDao;
import com.enablix.app.content.ContentDataManager;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;
import com.enablix.core.commons.xsdtopojo.CandidateContainersType;
import com.enablix.core.commons.xsdtopojo.CorrelatedEntitiesType;
import com.enablix.core.commons.xsdtopojo.EntityContentType;
import com.enablix.core.commons.xsdtopojo.FilterTagsType;
import com.enablix.core.correlation.ItemItemCorrelation;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class CorrelatedEntityEmailContentResolver implements EmailContentResolver<CorrelatedEntitiesType> {

	@Autowired
	private ItemItemCorrelationDao dao;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Override
	public List<ContentDataRecord> getEmailContent(CorrelatedEntitiesType corrEntitiesDef,
			TemplateWrapper template, ContentDataRef triggerItem) {

		List<ItemItemCorrelation> itemCorrs = new ArrayList<>();
		
		List<EntityContentType> corrEntityDef = corrEntitiesDef.getEntity();
		
		if (corrEntityDef != null && !corrEntityDef.isEmpty()) {
			
			for (EntityContentType entityContentType : corrEntityDef) {
				
				CandidateContainersType contentTypes = entityContentType.getContentTypes();
				FilterTagsType filterTags = entityContentType.getFilterTags();
				
				List<String> relatedContQIds = contentTypes != null ? 
						contentTypes.getContainerQId() : new ArrayList<String>();
						
				List<String> filterTagNames = filterTags != null ? filterTags.getTag() : new ArrayList<String>();
				
				itemCorrs.addAll(dao.findByItemAndRelatedItemQIdAndContainingTags(
						triggerItem, relatedContQIds, filterTagNames));
			}
			
		} else {
			itemCorrs.addAll(dao.findByItemAndContainingTags(triggerItem, new ArrayList<String>()));
		}
		
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
		
		List<ContentDataRecord> emailContent = new ArrayList<>();
		for (Map.Entry<String, List<String>> contentIdentities : contentIdentitiesMap.entrySet()) {
			
			String relatedItemQId = contentIdentities.getKey();
			List<Map<String, Object>> contentRecords = contentDataMgr.getContentRecords(
					relatedItemQId, contentIdentities.getValue(), template);
			
			for (Map<String, Object> contentRecord : contentRecords) {
				ContentDataRecord record = new ContentDataRecord(template.getId(), 
						relatedItemQId, contentRecord);
				emailContent.add(record);
			}
		}
		
		return emailContent;
	}

	@Override
	public boolean canHandle(BaseEmailContentType contentDef) {
		return contentDef instanceof CorrelatedEntitiesType;
	}

}
