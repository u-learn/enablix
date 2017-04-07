package com.enablix.data.view.mongo.coll;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.enablix.commons.content.ContentParser;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.core.domain.segment.DataSegmentAttribute.Matching;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchCondition;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.services.util.ContentDataUtil;

public class AbstractAttributeMatcher {

	public AbstractAttributeMatcher() {
		super();
	}

	protected boolean matchAttrValue(Map<String, Object> record, DataSegmentAttribute dsAttr, String recordAttrId) {
		
		List<Object> contentValue = ContentParser.getValue(record, recordAttrId);
		
		if (CollectionUtil.isEmpty(contentValue)) {
			
			if (dsAttr.getMatching() == Matching.LENIENT) {
				return true;
			} else {
				return false;
			}
			
		}
		
		Object segmentValue = dsAttr.getValue();
		
		List<String> txContentValue = ContentDataUtil.checkAndConvertToIdOrIdentityCollection(contentValue);
		if (txContentValue != null) {
			
			if (segmentValue instanceof String) {
				return txContentValue.contains(segmentValue);
			}
			
			if (segmentValue instanceof Collection<?>) {
				List<String> txSegmentValue = ContentDataUtil.checkAndConvertToIdOrIdentityCollection(segmentValue);
				return !CollectionUtil.intersection(txContentValue, txSegmentValue).isEmpty();
			}
		}
		
		return false;
	}

	protected SearchCondition<?> attributeFilter(DataSegmentAttribute dsAttr, String recordAttrId) {
		
		SearchCondition<?> searchFilter = null;
		
		Object segmentValue = dsAttr.getValue();
		
		if (segmentValue instanceof String) {
			
			StringFilter strFilter = new StringFilter(recordAttrId, (String) segmentValue, ConditionOperator.EQ);
			if (dsAttr.getMatching() == Matching.LENIENT) {
				strFilter.setExistsCheck(false);
			}
			
			searchFilter = strFilter;
			
		} else {
			
			List<String> txSegmentValue = ContentDataUtil.checkAndConvertToIdOrIdentityCollection(segmentValue);
			
			if (CollectionUtil.isNotEmpty(txSegmentValue)) {
				
				StringListFilter strListFilter = new StringListFilter(recordAttrId, txSegmentValue, ConditionOperator.IN);
				
				if (dsAttr.getMatching() == Matching.LENIENT) {
					strListFilter.setExistsCheck(false);
				}
				
				searchFilter = strListFilter;
			}
		}
		
		/*if (dsAttr.getMatching() == Matching.LENIENT) {
			searchFilter = searchFilter.or(new ObjectFilter(recordAttrId, false, ConditionOperator.EXISTS));
		}*/
		
		return searchFilter;
	}

}