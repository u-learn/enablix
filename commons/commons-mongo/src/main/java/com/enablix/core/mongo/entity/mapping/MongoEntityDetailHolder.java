package com.enablix.core.mongo.entity.mapping;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContextEvent;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.stereotype.Component;

@Component
public class MongoEntityDetailHolder implements ApplicationListener<MappingContextEvent<?, ?>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoEntityDetailHolder.class);
	
	private Map<String, MongoPersistentEntity<?>> persistentEntities;
	
	public MongoEntityDetailHolder() {
		this.persistentEntities = new HashMap<>();
	}
	
	@Override
	public void onApplicationEvent(MappingContextEvent<?, ?> event) {
		
		PersistentEntity<?, ?> entity = event.getPersistentEntity();

		// Double check type as Spring infrastructure does not consider nested generics
		if (entity instanceof MongoPersistentEntity) {
			MongoPersistentEntity<?> persistentEntity = (MongoPersistentEntity<?>) entity;
			String collName = persistentEntity.getCollection();
			
			LOGGER.debug("Entity loaded: {}, collection: {}", persistentEntity.getName(), collName);
			
			persistentEntities.put(collName, persistentEntity);

		}
		
	}
	
	
	public MongoPersistentEntity<?> getPersistentEntity(String collName) {
		return persistentEntities.get(collName);
	}
	
}
