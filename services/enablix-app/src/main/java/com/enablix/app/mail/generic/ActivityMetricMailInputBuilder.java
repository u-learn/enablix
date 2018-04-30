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

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.report.activity.metric.ActivityMetricService;
import com.enablix.app.report.activity.metric.metricesimpl.UserRegistrationMetric;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.mail.entities.EmailRequest;
import com.enablix.core.mail.entities.EmailRequest.Recipient;
import com.enablix.core.mail.velocity.AbstractEmailVelocityInputBuilder;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.data.view.DataView;

@Component
public class ActivityMetricMailInputBuilder extends AbstractEmailVelocityInputBuilder<ActivityMetricMailInput> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricMailInputBuilder.class);
	
	@Autowired
	private ActivityMetricService metricService;
	
	@Autowired
	private UserRegistrationMetric userRegistrationMetric;
	
	@Autowired
	private VelocityTemplateInputResolverFactory factory;
	
	@Override
	protected ActivityMetricMailInput buildInput(ActivityMetricMailInput input, EmailRequest request,
			DataView dataView) {
		
		Date startDate = getInputDate(request, GenericMailConstants.IN_DATA_METRIC_START_DATE);
		
		if (startDate == null) {
			LOGGER.error("Required parameter [{}] missing", GenericMailConstants.IN_DATA_METRIC_START_DATE);
			throw new IllegalArgumentException("Required parameter [" + GenericMailConstants.IN_DATA_METRIC_START_DATE + "] missing");
		}
		
		
		Date endDate = getInputDate(request, GenericMailConstants.IN_DATA_METRIC_END_DATE);;
		
		if (endDate == null) {
			endDate = Calendar.getInstance().getTime();
		}
		
		List<MetricStats> metricStats = metricService.getAggregatedValues(startDate, endDate, request.getDataFilter());
		metricStats.add(userRegistrationMetric.getUserCount(request.getDataFilter()));

		Map<String, Object> metricValues = new HashMap<>();
		metricStats.forEach((stat) -> { metricValues.put(stat.getMetricCode(), stat.getMetricValue()); });
		
		input.setMetricValues(metricValues);
		
		return input;
	}

	private Date getInputDate(EmailRequest request, String inParamName) {
		
		Object inDate = request.getInputData().get(inParamName);
		
		Date date = null;
		
		try {
			
			date = DateUtil.checkAndParseJsonDate(inDate);
			
		} catch (ParseException e) {
			
			LOGGER.error("Invalid value for [{}]: {}. Date should be in [{}] format", 
					inParamName, inDate, DateUtil.JSON_INPUT_DATE_FORMAT);
			throw new IllegalArgumentException("Invalid value for [" + inParamName + "]: " + inDate);
		}
		
		return date;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void processInputForRecipient(Recipient recipient, 
			ActivityMetricMailInput input, DataView dataView, DisplayContext ctx) {
		
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

	@Override
	protected ActivityMetricMailInput createInputInstance() {
		return new ActivityMetricMailInput();
	}

}
