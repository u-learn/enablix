package com.enablix.app.report.activity.metric;

import java.util.Date;

import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ActivityMetricRepository extends BaseMongoRepository<ActivityMetric> {
	ActivityMetric findByAsOfDate(Date asOfDate);
}
