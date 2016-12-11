package com.enablix.analytics.correlation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.CorrelationConstants;
import com.enablix.analytics.correlation.ItemUserCorrelator;
import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.app.content.ContentDataManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentCreatorCorrelator implements ItemUserCorrelator {

	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private ContentCrudService crudService;
	
	@Autowired
	private ItemUserCorrelationRecorder userCorrRecorder;
	
	@Override
	public void correlateUsers(ContentDataRef item, CorrelationContext context) {
		
		ContentTemplate template = context.getTemplate();
		
		Map<String, Object> contentRecord = contentDataMgr.getContentRecord(item, template);
		String creatorEmail = (String) contentRecord.get(ContentDataConstants.CREATED_BY_KEY);
		
		String userContainerQId = TemplateUtil.getUserContainerQId(template);
		String userCollectionName = TemplateUtil.resolveCollectionName(template, userContainerQId);
		
		String userEmailAttrId = TemplateUtil.getUserContainerEmailAttrId(template);
		
		StringFilter emailIdFilter = new StringFilter(userEmailAttrId, creatorEmail, ConditionOperator.EQ);
		
		List<Map<String, Object>> userRecords = crudService.findAllRecordForCriteria(userCollectionName, emailIdFilter.toPredicate(null));
		
		if (CollectionUtil.isNotEmpty(userRecords)) {
			Map<String, Object> user = userRecords.get(0);
			ContentDataRef userRef = ContentDataUtil.contentDataToRef(user, template, userContainerQId);
			
			List<String> tags = new ArrayList<>();
			tags.add(CorrelationConstants.CONTENT_CREATOR_ITEM_USER_CORR_TAG);
			
			userCorrRecorder.recordItemUserCorrelation(item, userRef, tags);
		}
		
	}

	@Override
	public void deleteCorrelationForItem(ContentDataRef item) {
		userCorrRecorder.removeItemUserCorrelation(item);
	}

}
