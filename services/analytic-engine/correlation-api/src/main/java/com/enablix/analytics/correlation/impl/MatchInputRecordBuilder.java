package com.enablix.analytics.correlation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.DataMatcher;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.app.content.ContentDataManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.FilterConstantType;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.core.commons.xsdtopojo.TriggerItemType;
import com.enablix.services.util.DataViewUtil;

@Component
public class MatchInputRecordBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchInputRecordBuilder.class);
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private DataMatcher dataMatcher;

	public MatchInputRecord buildTriggerMatchInput(TemplateFacade template, 
			TriggerItemType triggerItemType, ContentDataRef triggerItem) {
		
		FilterCriteriaType filterCriteria = triggerItemType.getFilterCriteria();
		if (filterCriteria != null) {
			if (!triggerRecordMatchesFilter(filterCriteria, template, triggerItem)) {
				return null;
			}
		}
		
		Map<String, Object> triggerItemRecord = contentDataMgr.getContentRecord(triggerItem, template, DataViewUtil.allDataView());
		
		MatchInputRecord matchInput = new MatchInputRecord(triggerItem.getContainerQId(), 
											triggerItem.getContainerQId(), triggerItemRecord);
		
		populateParent(matchInput, template);
		
		return matchInput;
	}
	
	private boolean triggerRecordMatchesFilter(FilterCriteriaType filterCriteria, TemplateFacade template,
			ContentDataRef triggerItem) {
		
		MatchInputRecord emptyMatchInput = new MatchInputRecord(triggerItem.getContainerQId(), 
				triggerItem.getContainerQId(), new HashMap<String, Object>());
		
		FilterCriteriaType triggerFilterCriteria = new FilterCriteriaType();

		// Add identity filter
		FilterType identityFilter = new FilterType();
		identityFilter.setAttributeId(ContentDataConstants.IDENTITY_KEY);
		
		FilterConstantType identityFilterConstantVal = new FilterConstantType();
		identityFilterConstantVal.setValue(triggerItem.getInstanceIdentity());
		identityFilter.setConstant(identityFilterConstantVal);
		
		triggerFilterCriteria.getFilter().add(identityFilter);
		
		// check that the record has made it to the persistence layer of the data matcher before 
		// matching the record against the filter criteria i.e. in case of elastic search data matcher,
		// need to make sure that the record has made it to the 
		List<Map<String, Object>> triggerRecord = 
				dataMatcher.findMatchingRecords(triggerItem.getContainerQId(), template, triggerFilterCriteria, emptyMatchInput);
		
		if (CollectionUtil.isEmpty(triggerRecord)) {
			LOGGER.error("Trigger record [{}] not found in data matcher lookup store", triggerItem);
			throw new DataSyncPendingException("Trigger record not found in data matcher lookup store");
		}
		
		// check if the trigger entity matches the filter criteria
		triggerFilterCriteria.getFilter().addAll(filterCriteria.getFilter());
		List<Map<String, Object>> matchedRecords = 
				dataMatcher.findMatchingRecords(triggerItem.getContainerQId(), template, triggerFilterCriteria, emptyMatchInput);
		
		return CollectionUtil.isNotEmpty(matchedRecords);
	}

	private void populateParent(MatchInputRecord matchInput, TemplateFacade template) {
		
		Map<String, Object> parentRecord = contentDataMgr.fetchParentRecord(
				template, matchInput.getContentQId(), matchInput.getRecord(), DataViewUtil.allDataView());
		
		if (parentRecord != null) {
			MatchInputRecord parentMI = new MatchInputRecord(matchInput.getTriggerItemQId(), 
					QIdUtil.getParentQId(matchInput.getContentQId()), parentRecord);
			matchInput.setParent(parentMI);
			
			populateParent(parentMI, template);
		}
		
	}
	
}
