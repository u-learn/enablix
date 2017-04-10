package com.enablix.data.view.mongo.coll;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.core.domain.segment.DataSegmentAttribute.Matching;
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
		
		SearchFilter dsInfoFilter = new ObjectFilter("dataSegmentInfo", false, ConditionOperator.EXISTS);
		
		SearchFilter searchFilter = null;
		
		for (DataSegmentAttrFilter filter : filters) {
			
			DataSegmentAttribute dsAttr = filter.getDataSegmentAttribute();
			
			SearchCondition<?> attrCond = attributeFilter(dsAttr, 
					"dataSegmentInfo.attributes." + filter.getRecordAttributeId());
			
			if (attrCond != null) {
				
				attrCond.setExistsCheck(null);
				
				SearchFilter attrFilter = attrCond;
				
				if (dsAttr.getMatching() == Matching.LENIENT) {
					attrFilter = attrCond.or(new StringFilter(
							"dataSegmentInfo.attributes." + dsAttr.getId(), 
							null, ConditionOperator.EQ));
				}
				
				if (searchFilter == null) {
					searchFilter = attrFilter;
				} else {
					searchFilter = searchFilter.and(attrFilter);
				}
			}
		}

		if (searchFilter != null) {
			dsInfoFilter = dsInfoFilter.or(searchFilter);
		}
		
		return dsInfoFilter;
	}

}
