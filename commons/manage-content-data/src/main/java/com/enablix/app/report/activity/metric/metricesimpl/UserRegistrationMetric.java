package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.core.mongo.search.service.SearchRequestTransformer;

@Component
public class UserRegistrationMetric {

	@Autowired
	private SearchRequestTransformer requestTx;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	

	public MetricStats getUserCount(SearchRequest userFilter) {

		Criteria criteria = requestTx.buildQueryCriteria(userFilter);
		
		Query query = Query.query(criteria);
		Long count = mongoTemplate.count(query, UserProfile.class);

		return new MetricStats(MetricType.USER_REGISTRATION.getCode(), MetricType.USER_REGISTRATION.getValue(), count, null);
	}

}
