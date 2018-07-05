package com.enablix.core.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.BasicConditionType;
import com.enablix.core.commons.xsdtopojo.CondValueType;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.SearchCondition;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class DBConditionEvaluator {

	@Autowired
	private GenericDao genericDao;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ContentDataRecord> findMatchingRecords(ConditionEvalContext context, 
			List<BasicConditionType> condition, MongoDataView view) {

		List<ContentDataRecord> contentRecords = new ArrayList<>();
		String templateId = ProcessContext.get().getTemplateId();
		
		Map<ContentRecordQueryGroupKey, List<CondValueType>> groupedRecords = groupContentRecord(condition);
		
		for (Map.Entry<ContentRecordQueryGroupKey, List<CondValueType>> entry : groupedRecords.entrySet()) {
			
			ContentRecordQueryGroupKey groupKey = entry.getKey();
			Criteria queryCriteria = createQuery(groupKey, entry.getValue());
			
			String collectionName = context.getCollectionName(groupKey.contentQId);
			
			List<Map> records = genericDao.findByCriteria(queryCriteria, collectionName, Map.class, view);
			for (Map<String, Object> rec : records) {
				contentRecords.add(new ContentDataRecord(templateId, groupKey.contentQId, rec));
			}
		}
			
		return contentRecords;
	}
	
	private Criteria createQuery(ContentRecordQueryGroupKey groupKey, List<CondValueType> values) {
		
		SearchCondition<?> filter = null;
		
		if (values.size() == 1) {
		
			filter = new StringFilter(groupKey.attribute, values.get(0).getValue(), ConditionOperator.EQ);
			
		} else if (values.size() > 1) {
			
			List<String> strValues = new ArrayList<>();
			for (CondValueType condVal : values) {
				strValues.add(condVal.getValue());
			}
			
			filter = new StringListFilter(groupKey.attribute, strValues, ConditionOperator.IN);
		}
		
		return filter == null ? null : filter.toPredicate(new Criteria());
	}
	
	private Map<ContentRecordQueryGroupKey, List<CondValueType>> 
					groupContentRecord(List<BasicConditionType> condition) {
		
		Map<ContentRecordQueryGroupKey, List<CondValueType>> grouped = new HashMap<>();
		
		for (BasicConditionType recordType : condition) {
		
			ContentRecordQueryGroupKey key = new ContentRecordQueryGroupKey(
					recordType.getOnItem(), recordType.getAttribute().getValue());
			
			List<CondValueType> existValues = grouped.get(key);
			if (existValues == null) {
				existValues = new ArrayList<>();
				grouped.put(key, existValues);
			}
			
			existValues.addAll(recordType.getValue());
		}
		
		return grouped;
	}
	
	private static class ContentRecordQueryGroupKey {
		
		private String contentQId;
		private String attribute;
		
		public ContentRecordQueryGroupKey(String contentQId, String attribute) {
			super();
			this.contentQId = contentQId;
			this.attribute = attribute;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
			result = prime * result + ((contentQId == null) ? 0 : contentQId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ContentRecordQueryGroupKey other = (ContentRecordQueryGroupKey) obj;
			if (attribute == null) {
				if (other.attribute != null)
					return false;
			} else if (!attribute.equals(other.attribute))
				return false;
			if (contentQId == null) {
				if (other.contentQId != null)
					return false;
			} else if (!contentQId.equals(other.contentQId))
				return false;
			return true;
		}
		
		
	}

	
}
