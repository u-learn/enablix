package com.enablix.analytics.correlation.data.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.core.mongo.dao.BaseTenantDao;
import com.enablix.core.mongo.search.And;
import com.enablix.core.mongo.search.CompositeFilter;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class ItemUserCorrelationDao extends BaseTenantDao {

	private static final String TAG_NAME_FILTER_PROP_NAME = "tags.name";
	private static final String ITEM_CONTAINER_QID_PROP_NAME = "item.containerQId";
	private static final String ITEM_INSTANCE_IDENTITY_PROP_NAME = "item.instanceIdentity";
	
	public List<ItemUserCorrelation> findByItemAndContainingTags(
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
		
		return findByCriteria(criteria, ItemUserCorrelation.class, view);
	}
	
}
