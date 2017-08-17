package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;
import com.enablix.commons.constants.AppConstants.MetricType;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.search.service.SearchRequest;

@Component
public class UserRegistrationMetric implements MetricStatsCalculator {

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	@Override
	public MetricType metricType() {
		return MetricType.USER_REGISTRATION;
	}

	@Override
	public MetricStats calculateStats(Date startDate, Date endDate, SearchRequest userFilter) {

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);
		
		Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		
		MetricStats metricStats = null;
		
		Query query = Query.query(criteria);
		Long count = mongoTemplate.count(query, UserProfile.class);

		metricStats = new MetricStats(metricType().getCode(), metricType().getValue(), count, startDate);

		return metricStats;
	}

	@Override
	public List<MetricStats> calculateDailyTrend(Date startDate, Date endDate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MetricStats> calculateWeeklyTrend(Date startDate, Date endDate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MetricStats> calculateMonthlyTrend(Date startDate, Date endDate) {
		throw new UnsupportedOperationException();
	}

}
