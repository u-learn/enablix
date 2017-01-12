package com.enablix.core.mongo.search.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.enablix.core.mongo.search.BoolFilter;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.DateFilter;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;

public class SearchRequest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchRequest.class);
	
	private Map<String, Object> filters = new HashMap<>();
	private Map<String, FilterMetadata> filterMetadata = new HashMap<>();
	private Pagination pagination;
	private List<String> projectedFields;
	
	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public Map<String, FilterMetadata> getFilterMetadata() {
		return filterMetadata;
	}

	public void setFilterMetadata(Map<String, FilterMetadata> filterMetadata) {
		this.filterMetadata = filterMetadata;
	}
	
	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<String> getProjectedFields() {
		return projectedFields;
	}

	public void setProjectedFields(List<String> projectedFields) {
		this.projectedFields = projectedFields;
	}

	public static class FilterMetadata {
		
		public enum DataType {
			STRING, DATE, BOOL, NUMBER
		}
		
		private String field;
		private ConditionOperator operator = ConditionOperator.EQ;
		private DataType dataType = DataType.STRING;
		
		public String getField() {
			return field;
		}
		
		public void setField(String field) {
			this.field = field;
		}
		
		public ConditionOperator getOperator() {
			return operator;
		}
		
		public void setOperator(ConditionOperator operator) {
			this.operator = operator;
		}
		
		public DataType getDataType() {
			return dataType;
		}
		
		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}
		
		public SearchFilter buildSearchFilter(Object filterValue) {
			
			SearchFilter filter = null;
			
			switch (dataType) {
				
				case DATE:
					DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
					try {
						Date date = formatter.parse(String.valueOf(filterValue));
						filter = new DateFilter(field, date, operator);
					} catch (ParseException e) {
						LOGGER.error("Error parsing date [" + String.valueOf(filterValue) +"]", e);
					}
					break;
					
				case BOOL:
					Boolean propValue = filterValue instanceof Boolean ? (Boolean) filterValue : 
											Boolean.valueOf(String.valueOf(filterValue));
					filter = new BoolFilter(field, propValue, operator);
					break;
					
				case STRING:
				default:
					filter = new StringFilter(field, String.valueOf(filterValue), operator);
					break;
			}
			
			return filter;
		}
		
	}
	
	public static class SortCriteria {
		
		private String field;
		private Direction direction = Direction.ASC;
		
		public String getField() {
			return field;
		}
		
		public void setField(String field) {
			this.field = field;
		}
		
		public Direction getDirection() {
			return direction;
		}
		
		public void setDirection(Direction direction) {
			this.direction = direction;
		}
		
		public Sort toSortObject() {
			return new Sort(direction, field);
		}
		
	}
	
	public static class Pagination {
		
		private int pageSize;
		private int pageNum;
		private SortCriteria sort;

		public int getPageSize() {
			return pageSize;
		}
		
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		
		public int getPageNum() {
			return pageNum;
		}
		
		public void setPageNum(int pageNum) {
			this.pageNum = pageNum;
		}
		
		public SortCriteria getSort() {
			return sort;
		}
		
		public void setSort(SortCriteria sort) {
			this.sort = sort;
		}
		
		public Pageable toPageableObject() {
			return new PageRequest(pageNum, pageSize, sort.toSortObject()); 
		}
		
	}
	
}
