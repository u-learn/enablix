package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.AnalyserRegistry;
import com.enablix.analytics.info.detection.InfoAnalyser;
import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class AnalyserRegistryImpl extends SpringBackedBeanRegistry<InfoAnalyser> implements AnalyserRegistry {

	private List<InfoAnalyser> orderedAnalysers = new ArrayList<>();
	
	@Override
	public Collection<InfoAnalyser> allAnalysers() {
		return orderedAnalysers;
	}

	@Override
	protected Class<InfoAnalyser> lookupForType() {
		return InfoAnalyser.class;
	}

	@Override
	protected void registerBean(InfoAnalyser bean) {
		orderedAnalysers.add(bean);
		Collections.sort(orderedAnalysers, (a1, a2) -> a1.level().compareTo(a2.level()) );
	}

}
