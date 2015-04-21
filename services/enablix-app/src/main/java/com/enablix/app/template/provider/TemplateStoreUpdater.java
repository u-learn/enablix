package com.enablix.app.template.provider;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class TemplateStoreUpdater extends SpringBackedAbstractFactory<TemplateLoader> implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		for (TemplateLoader loader : registeredInstances()) {
			loader.loadAllTemplates();
		}
	}

	@Override
	protected Class<TemplateLoader> lookupForType() {
		return TemplateLoader.class;
	}


}
