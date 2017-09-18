package com.enablix.app.content.quality;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.app.content.QualityAlertProcessor;
import com.enablix.app.content.QualityAlertProcessorFactory;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class QualityAlertProcessorFactoryImpl extends SpringBackedBeanRegistry<QualityAlertProcessor> implements QualityAlertProcessorFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(QualityAlertProcessorFactoryImpl.class);
	
	private Map<QualityAlertProcessing, QualityAlertProcessor> registry = new HashMap<>();
	
	@Override
	public QualityAlertProcessor getProcessor(QualityAlertProcessing alertProcessing) {
		
		QualityAlertProcessor processor = registry.get(alertProcessing);
		
		if (processor == null) {
			LOGGER.error("No processor found for [{}]", alertProcessing);
			throw new IllegalArgumentException("No processor found for [" + alertProcessing + "]");
		}
		
		return processor;
	}

	@Override
	protected Class<QualityAlertProcessor> lookupForType() {
		return QualityAlertProcessor.class;
	}

	@Override
	protected void registerBean(QualityAlertProcessor bean) {
		registry.put(bean.handles(), bean);
	}

}
