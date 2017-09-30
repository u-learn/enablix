package com.enablix.analytics.info.detection.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.InfoDetectionConfigProvider;
import com.enablix.analytics.info.detection.InfoDetectionConfigProviderFactory;
import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class InfoDetectionConfigProviderFactoryImpl extends SpringBackedBeanRegistry<InfoDetectionConfigProvider> 
			implements InfoDetectionConfigProviderFactory {

	private Map<String, InfoDetectionConfigProvider> registry = new HashMap<>();
	
	@Override
	protected Class<InfoDetectionConfigProvider> lookupForType() {
		return InfoDetectionConfigProvider.class;
	}

	@Override
	protected void registerBean(InfoDetectionConfigProvider bean) {
		registry.put(bean.infoType(), bean);
	}

	@Override
	public InfoDetectionConfigProvider getConfigProvider(String infoType) {
		return registry.get(infoType);
	}

}
