package com.enablix.app.main;

import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

import com.enablix.core.mongo.MultiTenantMongoDbFactory;
import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig {

	@Autowired
	private MongoProperties properties;

	@Autowired(required = false)
	private MongoClientOptions options;

	private Mongo mongo;

	@PreDestroy
	public void close() {
		if (this.mongo != null) {
			this.mongo.close();
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public Mongo mongo() throws UnknownHostException {
		this.mongo = this.properties.createMongoClient(this.options);
		return this.mongo;
	}

	@Bean
	public MongoDbFactory multiTenantMongoDbFactory() throws UnknownHostException {
		return new MultiTenantMongoDbFactory(mongo(), AppConstants.BASE_DB_NAME);
	}
	
	
}
