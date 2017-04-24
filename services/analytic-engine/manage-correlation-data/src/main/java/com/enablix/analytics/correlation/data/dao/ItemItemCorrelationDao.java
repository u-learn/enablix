package com.enablix.analytics.correlation.data.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.correlation.ItemItemCorrelation;
import com.enablix.core.mongo.dao.BaseTenantDao;
import com.enablix.core.mongo.search.And;
import com.enablix.core.mongo.search.CompositeFilter;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.Or;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class ItemItemCorrelationDao extends BaseTenantDao {

	private static final String TAG_NAME_FILTER_PROP_NAME = "tags.name";
	private static final String ITEM_CONTAINER_QID_PROP_NAME = "item.containerQId";
	private static final String ITEM_INSTANCE_IDENTITY_PROP_NAME = "item.instanceIdentity";
	private static final String RELATED_ITEM_CONTAINER_QID_PROP_NAME = "relatedItem.containerQId";
	private static final String RECORD_ID_FIELD = "_id";
	
	public List<ItemItemCorrelation> findByItemAndContainingTags(
			ContentDataRef item, List<String> tags, MongoDataView view) {
		
		SearchFilter searchFilter = new StringFilter(ITEM_CONTAINER_QID_PROP_NAME, 
				item.getContainerQId(), ConditionOperator.EQ);
		
		CompositeFilter andFilter = new And(searchFilter, new StringFilter(
				ITEM_INSTANCE_IDENTITY_PROP_NAME, item.getInstanceIdentity(), ConditionOperator.EQ));
		
		searchFilter = andFilter;
		
		for (String tag : tags) {
			
			SearchFilter tagFilter = new StringFilter(TAG_NAME_FILTER_PROP_NAME, tag, ConditionOperator.EQ);
			
			if (searchFilter == null) {
				searchFilter = tagFilter;
			} else {
				searchFilter = searchFilter.and(tagFilter);
			}
		}
		
		Criteria criteria = searchFilter != null ? searchFilter.toPredicate(new Criteria()) : new Criteria();
		
		return findByCriteria(criteria, ItemItemCorrelation.class, view);
	}
	
	public List<ItemItemCorrelation> findByItemAndRelatedItemQIdAndContainingTags(
			ContentDataRef item, List<String> relatedItemContainerQId, List<String> tags, MongoDataView view) {
		
		SearchFilter searchFilter = new StringFilter(ITEM_CONTAINER_QID_PROP_NAME, 
				item.getContainerQId(), ConditionOperator.EQ);
		
		CompositeFilter andFilter = new And(searchFilter, new StringFilter(
				ITEM_INSTANCE_IDENTITY_PROP_NAME, item.getInstanceIdentity(), ConditionOperator.EQ));
		
		searchFilter = andFilter;
		
		// Cannot add two 'or' criteria in same query due to Mongo Java API limitation
		// http://stackoverflow.com/questions/38519448/mongodb-due-to-limitations-of-the-com-mongodb-basicdbobject-you-cant-add-a-s
		// Hence two separate queries to arrive at final list of records
		
		List<ItemItemCorrelation> filteredRecords = null;
		
		// first filter on the tags
		if (CollectionUtil.isNotEmpty(tags)) {
			SearchFilter tagsFilter = getOrFilter(TAG_NAME_FILTER_PROP_NAME, tags);
			searchFilter = searchFilter.and(tagsFilter);
			
			filteredRecords = findByCriteria(searchFilter.toPredicate(new Criteria()), ItemItemCorrelation.class, view);
		}
		
		// now filter on the related Item container qualified id
		if (CollectionUtil.isNotEmpty(relatedItemContainerQId)) {
			
			if (CollectionUtil.isNotEmpty(tags) && CollectionUtil.isNotEmpty(filteredRecords)) {
				
				// if tag filter was present and records matched the tag filter, then apply 
				
				ArrayList<String> recordIds = CollectionUtil.transform(filteredRecords, 
												() -> new ArrayList<String>(), (in) -> in.getId());
			
				searchFilter = new StringListFilter(RECORD_ID_FIELD, recordIds, ConditionOperator.IN);
				
				SearchFilter relatedItemsFilter = getOrFilter(RELATED_ITEM_CONTAINER_QID_PROP_NAME, relatedItemContainerQId);
				searchFilter = searchFilter.and(relatedItemsFilter);
				
				Criteria criteria = searchFilter.toPredicate(new Criteria());
				filteredRecords = findByCriteria(criteria, ItemItemCorrelation.class, view);
				
			} else if (CollectionUtil.isEmpty(tags)) {
				
				// if tags filter was missing, then add related container qualified id filter
				
				SearchFilter relatedItemsFilter = getOrFilter(RELATED_ITEM_CONTAINER_QID_PROP_NAME, relatedItemContainerQId);
				searchFilter.and(relatedItemsFilter);
				
				Criteria criteria = searchFilter.toPredicate(new Criteria());
				filteredRecords = findByCriteria(criteria, ItemItemCorrelation.class, view);
			}
			
			// if tags filter was present and did not match any record, then we do not need to run any more query
			
		} 
		
		// if both are empty, then execute with tag or related item filter
		if (CollectionUtil.isEmpty(tags) && CollectionUtil.isEmpty(relatedItemContainerQId)) {
			filteredRecords = findByCriteria(searchFilter.toPredicate(new Criteria()), ItemItemCorrelation.class, view);
		}
		
		return filteredRecords == null ? new ArrayList<ItemItemCorrelation>() : filteredRecords;
	}
	
	private SearchFilter getOrFilter(String propName, List<String> values) {
		
		if (CollectionUtil.isNotEmpty(values)) {
		
			SearchFilter filter = null; 
			
			for (String val : values) {
			
				StringFilter valFilter = new StringFilter(propName, val, ConditionOperator.EQ);
				
				if (filter == null) {
					filter = valFilter;
				} else if (filter instanceof Or) {
					filter = filter.or(valFilter);
				} else {
					filter = new Or(filter, valFilter);
				}
			}
			
			return filter;
		}
		
		return null;
	}
	
	
}
