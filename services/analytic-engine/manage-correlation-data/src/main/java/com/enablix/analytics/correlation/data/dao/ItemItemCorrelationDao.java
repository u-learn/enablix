package com.enablix.analytics.correlation.data.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.correlation.ItemItemCorrelation;
import com.enablix.core.mongo.search.And;
import com.enablix.core.mongo.search.CompositeFilter;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.Or;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;

@Component
public class ItemItemCorrelationDao extends BaseCorrelationDao {

	private static final String TAG_NAME_FILTER_PROP_NAME = "tags.name";
	private static final String ITEM_CONTAINER_QID_PROP_NAME = "item.containerQId";
	private static final String ITEM_INSTANCE_IDENTITY_PROP_NAME = "item.instanceIdentity";
	private static final String RELATED_ITEM_CONTAINER_QID_PROP_NAME = "relatedItem.containerQId";
	
	public List<ItemItemCorrelation> findByItemAndContainingTags(ContentDataRef item, List<String> tags) {
		
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
		
		return findByCriteria(criteria, ItemItemCorrelation.class);
	}
	
	public List<ItemItemCorrelation> findByItemAndRelatedItemQIdAndContainingTags(
			ContentDataRef item, List<String> relatedItemContainerQId, List<String> tags) {
		
		SearchFilter searchFilter = new StringFilter(ITEM_CONTAINER_QID_PROP_NAME, 
				item.getContainerQId(), ConditionOperator.EQ);
		
		CompositeFilter andFilter = new And(searchFilter, new StringFilter(
				ITEM_INSTANCE_IDENTITY_PROP_NAME, item.getInstanceIdentity(), ConditionOperator.EQ));
		
		searchFilter = andFilter;
		
		SearchFilter tagsFilter = getOrFilter(TAG_NAME_FILTER_PROP_NAME, tags);
		searchFilter.and(tagsFilter);
		
		SearchFilter relatedItemsFilter = getOrFilter(RELATED_ITEM_CONTAINER_QID_PROP_NAME, relatedItemContainerQId);
		searchFilter.and(relatedItemsFilter);
		
		Criteria criteria = searchFilter != null ? searchFilter.toPredicate(new Criteria()) : new Criteria();
		
		return findByCriteria(criteria, ItemItemCorrelation.class);
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
		}
		
		return null;
	}
	
	
}
