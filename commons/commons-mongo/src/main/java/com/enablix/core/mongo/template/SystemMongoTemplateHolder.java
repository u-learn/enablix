package com.enablix.core.mongo.template;

import org.springframework.data.mongodb.core.MongoTemplate;

public class SystemMongoTemplateHolder {

	private static MongoTemplate systemMongoTemplate;
	
	public static void registerSystemMongoTemplate(MongoTemplate mongoTemplate) {
		systemMongoTemplate = mongoTemplate;
	}
	
	public static MongoTemplate getSystemMongoTemplate() {
		return systemMongoTemplate;
	}
	
}
