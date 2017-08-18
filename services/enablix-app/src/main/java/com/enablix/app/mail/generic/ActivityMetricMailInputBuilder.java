package com.enablix.app.mail.generic;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.app.report.activity.metric.ActivityMetricService;
import com.enablix.app.report.activity.metric.metricesimpl.UserRegistrationMetric;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.data.view.DataView;

@Component
public class ActivityMetricMailInputBuilder implements GenericEmailVelocityInputBuilder<ActivityMetricMailInput> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricMailInputBuilder.class);
	
	@Autowired
	private ActivityMetricService metricService;
	
	@Autowired
	private UserRegistrationMetric userRegistrationMetric;
	
	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@Override
	public ActivityMetricMailInput build(EmailRequest request, DataView dataView) {
		
		ActivityMetricMailInput input = new ActivityMetricMailInput();
		input.setInputData(request.getInputData());
		
		String startDateStr = (String) request.getInputData().get("metricStartDate");
		
		Date startDate = null;
		
		try {
			
			startDate = DateUtil.parseJsonInputDate(startDateStr);
			
		} catch (ParseException e) {
			
			LOGGER.error("Invalid value for [metricStartDate]: {}. Date should be in [{}] format", 
					startDateStr, DateUtil.JSON_INPUT_DATE_FORMAT);
			throw new IllegalArgumentException("Invalid value for [metricStartDate]: " + startDateStr);
		}
		
		if (startDate == null) {
			LOGGER.error("Required parameter [metricStartDate] missing");
			throw new IllegalArgumentException("Required parameter [metricStartDate] missing");
		}
		
		Date endDate = Calendar.getInstance().getTime();
		
		List<MetricStats> metricStats = metricService.getAggregatedValues(startDate, endDate, request.getDataFilter());
		metricStats.add(userRegistrationMetric.getUserCount(request.getDataFilter()));

		Map<String, Object> metricValues = new HashMap<>();
		metricStats.forEach((stat) -> { metricValues.put(stat.getMetricCode(), stat.getMetricValue()); });
		
		input.setMetricValues(metricValues);
		
		return input;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void processInputForRecipient(Recipient recipient, ActivityMetricMailInput input, DataView dataView) {
		
		input.setRecipientUserId(recipient.getEmailId());

		Collection<VelocityTemplateInputResolver<ActivityMetricMailInput>> resolvers = 
				factory.getResolvers(input);
		
		for (VelocityTemplateInputResolver resolver : resolvers) {
			resolver.work(input, dataView);
		}
	}

	@Override
	public String mailType() {
		return GenericMailConstants.MAIL_TYPE_ACTIVITY_METRIC;
	}

}
