package com.enablix.data.view.mongo.coll;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.core.domain.segment.DataSegmentAttribute.Matching;
import com.enablix.core.domain.segment.DataSegmentAttribute.Presence;
import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.ObjectFilter;
import com.enablix.core.mongo.search.SearchCondition;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.mongo.AttributeMatcher;

@Component
public class DataSegmentInfoAttributeMatcher extends AbstractAttributeMatcher implements AttributeMatcher<DataSegmentAware> {

	@Override
	public boolean matchAttributes(DataSegmentAware record, List<DataSegmentAttrFilter> dsFilters) {
		
		boolean matches = true;
		
		DataSegmentInfo dsInfo = record.getDataSegmentInfo();
		
		if (dsInfo != null) {
			
			Map<String, Object> attributes = dsInfo.getAttributes();
			
			for (DataSegmentAttrFilter filter : dsFilters) {
				
				matches = matchAttrValue(attributes, filter.getDataSegmentAttribute(), filter.getRecordAttributeId());
				
				// return false, when first un-matched attribute found
				if (!matches) {
					break;
				}
			}
		}
		
		return matches;
		
	}

	@Override
	public SearchFilter attributeFilters(List<DataSegmentAttrFilter> filters) {
		
		SearchFilter searchFilter = null;
		
		for (DataSegmentAttrFilter filter : filters) {
			
			DataSegmentAttribute dsAttr = filter.getDataSegmentAttribute();
			
			SearchCondition<?> attrCond = attributeFilter(dsAttr, 
					"dataSegmentInfo.attributes." + filter.getRecordAttributeId());
			
			if (attrCond != null) {
				
				// set null so that the current filter is only matching values
				attrCond.setExistsCheck(null);
				
				SearchFilter attrFilter = attrCond;
				
				String attributeObjectFilterId = "dataSegmentInfo.attributes." + dsAttr.getId();
				
				/*
				 * Attribute matching rules:
				 * 
				 * OPTIONAL+STRICT  : <not-exist> OR <match>
				 * OPTIONAL+LENIENT : <not-exist> OR <null> OR <match>
				 * REQUIRED+STRICT  : <match>
				 * REQUIRED+LENIENT : <null> OR <match>
				 * 
				 */
				if (dsAttr.getPresence() == Presence.REQUIRED) {
					
					if (dsAttr.getMatching() == Matching.LENIENT) {

						StringFilter nullFilter = new StringFilter(attributeObjectFilterId, 
															null, ConditionOperator.EQ);
						
						attrFilter = attrCond.or(nullFilter);
					}
						
				} else {
					
					if (dsAttr.getMatching() == Matching.LENIENT) {
						
						StringFilter nullFilter = new StringFilter(attributeObjectFilterId, 
								null, ConditionOperator.EQ);
						nullFilter.setExistsCheck(false);

						attrFilter = attrCond.or(nullFilter);

					} else {
						
						ObjectFilter objFilter = new ObjectFilter(attributeObjectFilterId, false, ConditionOperator.EXISTS);
						attrFilter = attrCond.or(objFilter);
					}
				}
					
				if (searchFilter == null) {
					searchFilter = attrFilter;
				} else {
					searchFilter = searchFilter.and(attrFilter);
				}
			}
		}

		return searchFilter;
	}

}
