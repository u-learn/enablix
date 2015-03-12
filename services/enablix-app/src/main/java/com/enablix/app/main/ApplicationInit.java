package com.enablix.app.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAutoConfiguration
@Configuration
@PropertySources({
		@PropertySource("file:${baseDir}/config/properties/mongodb.properties"),
		@PropertySource("file:${baseDir}/config/properties/app.properties"),
		@PropertySource("file:${baseDir}/config/properties/security.properties"),
		@PropertySource("file:${baseDir}/config/properties/log4j.properties")})
@ComponentScan(basePackages = { "com.enablix" })
@EnableMongoRepositories(basePackages = {"com.enablix.app.mongo.repository"})
public class ApplicationInit {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationInit.class, args);
	}
	
}
