package com.enablix.app.report.activity.metric.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.metric.ActivityMetricConfigRepository;
import com.enablix.app.report.activity.metric.ActivityMetricRepository;
import com.enablix.app.report.activity.metric.ActivityMetricService;
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

		Calendar backRunCalendar = getCalendar();
		final Calendar currentDayCalendar = getCalendar();

		ActivityMetric activityMetricBean;
		List<MetricStats> metricStats;
		activityMetricConfigLoop : for (ActivityMetricConfig activityMetricConfig : activityMetrics) {
			backRunCalendar.setTime(activityMetricConfig.getNextRunDate());
			while(!DateUtils.isSameDay(backRunCalendar,currentDayCalendar)){

				MetricStats metricStat = activityMetric.executeActivityMetrices(activityMetricConfig,backRunCalendar.getTime());
				if(metricStat == null) {
					continue activityMetricConfigLoop;
				}

				activityMetricBean  = activityMetricRepo.findByAsOfDate(backRunCalendar.getTime());

				if(activityMetricBean == null){
					activityMetricBean = new ActivityMetric();
					metricStats = new ArrayList<MetricStats>();
					activityMetricBean.setAsOfDate(backRunCalendar.getTime());
				}
				else{
					metricStats = activityMetricBean.getMetricStats();
				}
				metricStats.add(metricStat);

				activityMetricBean.setMetricStats(metricStats);
				activityMetricRepo.save(activityMetricBean);

				backRunCalendar.add(Calendar.DATE, 1);
			}
			activityMetricConfig.setNextRunDate(backRunCalendar.getTime());
			activityMetricConfigRepo.save(activityMetricConfig);
		}
	}

	private Calendar getCalendar() {
		Calendar instance =  DateUtils.truncate(Calendar.getInstance(), java.util.Calendar.DAY_OF_MONTH);
		return instance;
	}

	@Override
	public String taskId() {
		return "activity-metric-calculator";
	}

}
