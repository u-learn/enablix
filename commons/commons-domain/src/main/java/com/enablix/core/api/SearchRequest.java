package com.enablix.core.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.enablix.commons.util.collection.CollectionUtil;

public class SearchRequest {
	
	private Map<String, Object> filters = new HashMap<>();
	private Map<String, FilterMetadata> filterMetadata = new HashMap<>();
	private Pagination pagination;
	private List<String> projectedFields;
	
	private String textQuery;
	
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
	
	public String getTextQuery() {
		return textQuery;
	}

	public void setTextQuery(String textQuery) {
		this.textQuery = textQuery;
	}


	public static class FilterMetadata {
		
		public enum DataType {
			STRING, DATE, BOOL, NUMBER
		}
		
		private String field;
		private ConditionOperator operator = ConditionOperator.EQ;
		private DataType dataType = DataType.STRING;
		private DateFilterConfig dateFilter;
		
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
		
		public DateFilterConfig getDateFilter() {
			return dateFilter;
		}

		public void setDateFilter(DateFilterConfig dateFilter) {
			this.dateFilter = dateFilter;
		}

	}
	
	
	
	public static class DateFilterConfig {
		
		public enum ValueType {
			LAST_X_DAYS
		}
		
		private ValueType valueType;

		public ValueType getValueType() {
			return valueType;
		}

		public void setValueType(ValueType valueType) {
			this.valueType = valueType;
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
		private SortCriteria sort; // for backward compatibility
		private List<SortCriteria> sorts; // multiple sorts, will ignore sort

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
		
		public List<SortCriteria> getSorts() {
			return sorts;
		}

		public void setSorts(List<SortCriteria> sorts) {
			this.sorts = sorts;
		}

		public Pageable toPageableObject() {
			
			Sort dataSort = null;
			
			if (CollectionUtil.isNotEmpty(sorts)) {
				for (SortCriteria sc : sorts) {
					dataSort = dataSort == null ? sc.toSortObject() : dataSort.and(sc.toSortObject());
				}
			} else if (sort != null) {
				dataSort = sort.toSortObject();
			}
			
			return new PageRequest(pageNum, pageSize, dataSort); 
		}
		
	}
	
}
