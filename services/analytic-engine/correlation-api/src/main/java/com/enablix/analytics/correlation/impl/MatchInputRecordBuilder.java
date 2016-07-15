package com.enablix.analytics.correlation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.DataMatcher;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.app.content.ContentDataManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FilterConstantType;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.core.commons.xsdtopojo.TriggerItemType;

@Component
public class MatchInputRecordBuilder {
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private DataMatcher dataMatcher;

	public MatchInputRecord buildTriggerMatchInput(ContentTemplate template, 
			TriggerItemType triggerItemType, ContentDataRef triggerItem) {
		
		FilterCriteriaType filterCriteria = triggerItemType.getFilterCriteria();
		if (filterCriteria != null) {
			if (!triggerRecordMatchesFilter(filterCriteria, template, triggerItem)) {
				return null;
			}
		}
		
		Map<String, Object> triggerItemRecord = contentDataMgr.getContentRecord(triggerItem, template);
		
		MatchInputRecord matchInput = new MatchInputRecord(triggerItem.getContainerQId(), 
											triggerItem.getContainerQId(), triggerItemRecord);
		
		populateParent(matchInput, template);
		
		return matchInput;
	}
	
	private boolean triggerRecordMatchesFilter(FilterCriteriaType filterCriteria, ContentTemplate template,
			ContentDataRef triggerItem) {
		
		MatchInputRecord emptyMatchInput = new MatchInputRecord(triggerItem.getContainerQId(), 
				triggerItem.getContainerQId(), new HashMap<String, Object>());
		
		FilterCriteriaType triggerFilterCriteria = new FilterCriteriaType();
		triggerFilterCriteria.getFilter().addAll(filterCriteria.getFilter());
		
		// Add identity filter
		FilterType identityFilter = new FilterType();
		identityFilter.setAttributeId(ContentDataConstants.IDENTITY_KEY);
		
		FilterConstantType identityFilterConstantVal = new FilterConstantType();
		identityFilterConstantVal.setValue(triggerItem.getInstanceIdentity());
		
		identityFilter.setConstant(identityFilterConstantVal);
		
		triggerFilterCriteria.getFilter().add(identityFilter);
		
		// check if the trigger entity matches the filter criteria
		List<Map<String, Object>> matchedRecords = 
				dataMatcher.findMatchingRecords(triggerItem.getContainerQId(), template, triggerFilterCriteria, emptyMatchInput);
		
		return CollectionUtil.isNotEmpty(matchedRecords);
		
	}

	private void populateParent(MatchInputRecord matchInput, ContentTemplate template) {
		
		Map<String, Object> parentRecord = contentDataMgr.fetchParentRecord(
				template, matchInput.getContentQId(), matchInput.getRecord());
		
		if (parentRecord != null) {
			MatchInputRecord parentMI = new MatchInputRecord(matchInput.getTriggerItemQId(), 
					QIdUtil.getParentQId(matchInput.getContentQId()), parentRecord);
			matchInput.setParent(parentMI);
			
			populateParent(parentMI, template);
		}
		
	}
	
}
