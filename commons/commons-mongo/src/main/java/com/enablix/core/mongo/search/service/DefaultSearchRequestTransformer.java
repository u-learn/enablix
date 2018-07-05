package com.enablix.core.mongo.search.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.core.api.SearchRequest;
import com.enablix.core.api.SearchRequest.DateFilterConfig;
import com.enablix.core.api.SearchRequest.DateFilterConfig.ValueType;
import com.enablix.core.api.SearchRequest.FilterMetadata;
import com.enablix.core.mongo.search.BoolFilter;
import com.enablix.core.mongo.search.DateFilter;
import com.enablix.core.mongo.search.NumericFilter;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.search.StringListFilter;

@Component
public class DefaultSearchRequestTransformer implements SearchRequestTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSearchRequestTransformer.class);
	
	public Criteria buildQueryCriteria(SearchRequest request) {
		
		SearchFilter queryFilter = null;
		
		Map<String, FilterMetadata> filterMetadata = request.getFilterMetadata();
		
		for (Map.Entry<String, Object> entry : request.getFilters().entrySet()) {
			
			FilterMetadata metadata = filterMetadata.get(entry.getKey());
			
			if (metadata == null) {
				metadata = new FilterMetadata();
				metadata.setField(entry.getKey());
			}
			
			SearchFilter searchFilter = buildSearchFilter(metadata, entry.getValue());
			
			if (queryFilter == null) {
				queryFilter = searchFilter;
			} else {
				queryFilter = queryFilter.and(searchFilter);
			}
		}
		
		Criteria criteria = new Criteria();
		if (queryFilter != null) {
			criteria = queryFilter.toPredicate(criteria);
		}
		
		return criteria;
		
	}

	@SuppressWarnings("unchecked")
	private SearchFilter buildSearchFilter(FilterMetadata metadata, Object filterValue) {
		SearchFilter filter = null;
		
		switch (metadata.getDataType()) {
			
			case DATE:
				
				filter = getDateFilter(metadata, filterValue);
				break;
				
			case BOOL:
				Boolean propValue = filterValue instanceof Boolean ? (Boolean) filterValue : 
										Boolean.valueOf(String.valueOf(filterValue));
				filter = new BoolFilter(metadata.getField(), propValue, metadata.getOperator());
				break;
				
			case NUMBER:
				
				Number numValue = null;
				
				if (filterValue instanceof Number) {
					numValue = (Number) filterValue;
				} else {
					numValue = Double.parseDouble(String.valueOf(filterValue));
				}
				
				filter = new NumericFilter(metadata.getField(), numValue, metadata.getOperator());
				break;
				
			case STRING:
				
				if (filterValue instanceof List) {
					filter = new StringListFilter(metadata.getField(), (List<String>) filterValue, metadata.getOperator());
					break;
				}
				
			default:
				filter = new StringFilter(metadata.getField(), String.valueOf(filterValue), metadata.getOperator());
				break;
		}
		
		return filter;
	}

	private DateFilter getDateFilter(FilterMetadata metadata, Object filterValue) {
		
		DateFilter filter = null;
		
		try {

			Date filterDate = null;
			
			
			DateFilterConfig dateFilter = metadata.getDateFilter();
			if (dateFilter != null && dateFilter.getValueType() == ValueType.LAST_X_DAYS) {

				int noOfDays = Integer.parseInt(String.valueOf(filterValue));
				Calendar date = Calendar.getInstance();
				date.add(Calendar.DAY_OF_MONTH, -noOfDays);
				filterDate = date.getTime();
				
			} else {
				
				DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
				filterDate = formatter.parse(String.valueOf(filterValue));
			}
			
			
			filter = new DateFilter(metadata.getField(), filterDate, metadata.getOperator());
			
		} catch (ParseException e) {
			LOGGER.error("Error parsing date [" + String.valueOf(filterValue) +"]", e);
		}
		
		return filter;
	}
	
}
