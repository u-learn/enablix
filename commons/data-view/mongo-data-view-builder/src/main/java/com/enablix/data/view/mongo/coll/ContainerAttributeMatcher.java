package com.enablix.data.view.mongo.coll;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.mongo.AttributeMatcher;

@Component
public class ContainerAttributeMatcher extends AbstractAttributeMatcher implements AttributeMatcher<Map<String, Object>> {

	@Override
	public boolean matchAttributes(Map<String, Object> record, List<DataSegmentAttrFilter> dsFilters) {
		
		boolean matches = true;
		
		for (DataSegmentAttrFilter filter : dsFilters) {
			
			matches = matchAttrValue(record, filter.getDataSegmentAttribute(), filter.getRecordAttributeId());
			
			// return false, when first un-matched attribute found
			if (!matches) {
				break;
			}
		}
		
		return matches;
	}

	@Override
	public SearchFilter attributeFilters(List<DataSegmentAttrFilter> filters) {
		
		SearchFilter searchFilter = null;
		
		for (DataSegmentAttrFilter filter : filters) {
			
			SearchFilter attrFilter = attributeFilter(
					filter.getDataSegmentAttribute(), filter.getRecordAttributeId());
			
			if (attrFilter != null) {
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
