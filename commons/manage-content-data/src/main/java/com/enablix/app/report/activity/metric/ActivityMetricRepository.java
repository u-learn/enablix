package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ActivityMetricRepository extends BaseMongoRepository<ActivityMetric> {
	
	ActivityMetric findByAsOfDate(Date asOfDate);

	List<ActivityMetric> findByAsOfDateBetween(Date startDate, Date endDate);
	
}