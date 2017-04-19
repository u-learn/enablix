package com.enablix.app.report.activity.metric.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.metric.ActivityMetricConfigRepository;
import com.enablix.app.report.activity.metric.ActivityMetricRepository;
import com.enablix.app.report.activity.metric.ActivityMetricService;
import com.enablix.app.report.util.ReportUtil;
import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class ActivityMetricCalculator implements Task {

	@Autowired
	private ActivityMetricService activityMetric;

	@Autowired
	private ActivityMetricRepository activityMetricRepo;

	@Autowired
	private ActivityMetricConfigRepository activityMetricConfigRepo;

	@Override
	public void run(TaskContext context) {

		List<ActivityMetricConfig> activityMetrics = activityMetric.getActivityMetricConfig();

		final Date currentDate = Calendar.getInstance().getTime();
		ActivityMetric activityMetricBean = new ActivityMetric();
		List<MetricStats> metricStats = new ArrayList<MetricStats>();

		for (ActivityMetricConfig activityMetricConfig : activityMetrics) {

			MetricStats metricStat = activityMetric.executeActivityMetrices(activityMetricConfig);

			activityMetricConfig.setRunDate(currentDate);
			activityMetricConfigRepo.save(activityMetricConfig);

			metricStats.add(metricStat);
		}

		activityMetricBean.setMetricStats(metricStats);
		activityMetricBean.setAsOfDate(currentDate);
		activityMetricRepo.save(activityMetricBean);
	}

	@Override
	public String taskId() {
		return "activity-metric-calculator";
	}

}
