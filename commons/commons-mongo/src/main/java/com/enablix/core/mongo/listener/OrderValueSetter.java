package com.enablix.core.mongo.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.enablix.core.api.OrderAware;
import com.enablix.core.mongo.counter.Counter;
import com.enablix.core.mongo.counter.CounterFactory;

@Component
public class OrderValueSetter extends AbstractMongoEventListener<OrderAware> {

	@Autowired
	private CounterFactory counterFactory;
	
	public void onBeforeConvert(BeforeConvertEvent<OrderAware> event) {
		
		if (event.getSource().getOrder() <= 0) {
			
			Counter counter = counterFactory.getCounter(event.getCollectionName());
			
			if (counter != null) {
				event.getSource().setOrder(counter.nextValue());
			}
		}
		
	}
}
