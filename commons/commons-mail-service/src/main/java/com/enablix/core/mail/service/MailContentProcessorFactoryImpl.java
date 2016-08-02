package com.enablix.core.mail.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class MailContentProcessorFactoryImpl extends SpringBackedBeanRegistry<MailContentProcessor> implements MailContentProcessorFactory {

	private Map<String, List<MailContentProcessor>> scenarioToProcessorsMap = new HashMap<>();
	
	@Override
	public List<MailContentProcessor> getProcessors(String mailScenarioName) {
		return scenarioToProcessorsMap.get(mailScenarioName);
	}

	@Override
	protected Class<MailContentProcessor> lookupForType() {
		return MailContentProcessor.class;
	}

	@Override
	protected void registerBean(MailContentProcessor bean) {
		for (String mailScenario : bean.interestedInScenarios()) {
			List<MailContentProcessor> processorList = scenarioToProcessorsMap.get(mailScenario);
			if (processorList == null) {
				processorList = new ArrayList<>();
				scenarioToProcessorsMap.put(mailScenario, processorList);
			}
			processorList.add(bean);
		}
	}

}
