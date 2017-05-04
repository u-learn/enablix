package com.enablix.core.mongo.counter.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.enablix.core.mongo.counter.CounterData;

public interface CounterDataRepository extends MongoRepository<CounterData, String> {

	CounterData findByCounterName(String counterName);
	
}
