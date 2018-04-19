package com.enablix.app.content.bulkimport;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class ImportProcessorFactoryImpl extends SpringBackedBeanRegistry<ImportProcessor> implements ImportProcessorFactory {

	private Map<String, ImportProcessor> registry = new HashMap<>();
	
	@Override
	public ImportProcessor getProcessor(String source) {
		return registry.get(source);
	}

	@Override
	protected Class<ImportProcessor> lookupForType() {
		return ImportProcessor.class;
	}

	@Override
	protected void registerBean(ImportProcessor bean) {
		registry.put(bean.importSource(), bean);
	}

}
