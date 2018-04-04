package com.enablix.core.mongo.counter;

public interface CounterFactory {

	Counter getCounter(String counterName);
	
	void checkAndCreateCounter(String counterName);
	
}
