package com.enablix.core.mq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.enablix.core.mq.EventBus;
import com.enablix.core.mq.impl.BeanEventHandlerRegisterer;
import com.enablix.core.mq.impl.SimpleEventBus;

@Configuration
public class EventConfiguration {

	@Bean
	@DependsOn("eventBus")
	public BeanEventHandlerRegisterer eventRegisterer() {
		return new BeanEventHandlerRegisterer();
	}
	
	@Bean 
	public EventBus eventBus() {
		return new SimpleEventBus();
	}
	
}
