package com.enablix.trigger.lifecycle.action.email;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class TriggerExecEntityMatchChecker {

	@Autowired
	private ContentCrudService crudService;
	
	public Boolean evaluateCondition(ContentChange contentChange, FilterCriteriaType entityMatchCond, TemplateWrapper template) {
		
		Boolean result = null;
		
		if (entityMatchCond != null) {
		
			List<FilterType> matchFilters = entityMatchCond.getFilter();
			
			if (CollectionUtil.isNotEmpty(matchFilters)) {

				SearchFilter filter = new StringFilter(ContentDataConstants.IDENTITY_KEY, 
						contentChange.getTriggerItem().getInstanceIdentity(), ConditionOperator.EQ);
				
				for (FilterType filterType : matchFilters) {
				
					StringFilter strFilter = new StringFilter(filterType.getAttributeId(), 
									filterType.getConstant().getValue(), ConditionOperator.EQ);
					filter = filter.and(strFilter);
				}
				
				String collectionName = template.getCollectionName(contentChange.getTriggerItem().getContainerQId());
				
				List<Map<String, Object>> matchedRecords = crudService.findAllRecordForCriteria(
						collectionName, filter.toPredicate(new Criteria()));
				
				result = CollectionUtil.isNotEmpty(matchedRecords);
				
			}
			
		}
		
		return result;
	}

}
