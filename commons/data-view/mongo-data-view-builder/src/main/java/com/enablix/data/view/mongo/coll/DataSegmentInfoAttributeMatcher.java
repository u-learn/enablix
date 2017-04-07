package com.enablix.data.view.mongo.coll;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.ObjectFilter;
import com.enablix.core.mongo.search.SearchCondition;
import com.enablix.core.mongo.search.SearchFilter;
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
			
			SearchCondition<?> attrCond = attributeFilter(
					filter.getDataSegmentAttribute(), filter.getRecordAttributeId());
			
			if (attrCond != null) {
				
				attrCond.setExistsCheck(false);
				
				if (searchFilter == null) {
					searchFilter = attrCond;
				} else {
					searchFilter = searchFilter.and(attrCond);
				}
			}
		}

		if (searchFilter != null) {
			dsInfoFilter = dsInfoFilter.or(searchFilter);
		}
		
		return dsInfoFilter;
	}

}
