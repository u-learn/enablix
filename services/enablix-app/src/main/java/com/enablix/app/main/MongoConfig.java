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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.mongo.MultiTenantMongoDbFactory;
import com.enablix.core.mongo.template.SystemMongoTemplateHolder;
import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
@EnableMongoRepositories(
		basePackages = {"com.enablix.core.system.repo", 
						"com.enablix.core.security.token.repo"},
		mongoTemplateRef = "systemMongoTemplate"
		)
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
	
	@Bean 
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		return new MongoTemplate(multiTenantMongoDbFactory());
	}
	
	@Bean(name="systemMongoTemplate")
	public MongoTemplate systemMongoTemplate() throws UnknownHostException {
		MongoTemplate systemMongoTemplate = 
				new MongoTemplate(mongo(), AppConstants.SYSTEM_DB_NAME);
		SystemMongoTemplateHolder.registerSystemMongoTemplate(systemMongoTemplate);
		return systemMongoTemplate;
	}
	
}
