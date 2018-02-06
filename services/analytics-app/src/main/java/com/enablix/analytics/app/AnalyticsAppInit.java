package com.enablix.analytics.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAutoConfiguration(exclude={ElasticsearchDataAutoConfiguration.class, ElasticsearchAutoConfiguration.class})
@Configuration
@PropertySources({
		@PropertySource("file:${baseDir}/config/properties/mongodb.properties"),
		@PropertySource("file:${baseDir}/config/properties/env.properties"),
		@PropertySource("file:${baseDir}/config/properties/app.properties"),
		@PropertySource("file:${baseDir}/config/properties/security.properties"),
		@PropertySource("file:${baseDir}/config/properties/elasticsearch.properties"),
		@PropertySource("file:${baseDir}/config/properties/mail.properties"),
		@PropertySource("file:${baseDir}/config/properties/log4j.properties"),
		@PropertySource("file:${baseDir}/config/properties/slack.properties"),
		@PropertySource("file:${baseDir}/config/properties/sharepoint.properties"),
		@PropertySource("file:${baseDir}/config/properties/doc-format.properties"),
		@PropertySource("file:${baseDir}/config/properties/analytic-engine.properties")})
@ComponentScan(basePackages = { "com.enablix" })
@EnableMongoRepositories(
		basePackages = {"com.enablix.app.mongo.repository", 
				"com.enablix.commons.dms.repository",
				"com.enablix.core.security.auth.repo",
				"com.enablix.analytics.recommendation.repository", 
				"com.enablix.core.mongo.config.repo",
				"com.enablix.app.content.recent.repo",
				"com.enablix.app.content.ui.link.repo",
				"com.enablix.analytics.correlation.repo",
				"com.enablix.analytics.correlation.data.repo",
				"com.enablix.trigger.lifecycle.rule.repo",
				"com.enablix.trigger.lifecycle.repo",
				"com.enablix.core.mongo.audit.repo",
				"com.enablix.content.mapping.repo",
				"com.enablix.doc.state.change.repo",
				"com.enablix.content.approval.repo",
				"com.enablix.content.connection.repo",
				"com.enablix.play.definition.repo",
				"com.enablix.app.content.summary.repo",
				"com.enablix.data.segment.repo",
				"com.enablix.app.report",
				"com.enablix.core.mongo.counter.repo",
				"com.enablix.user.pref.repo",
				"com.enablix.core.mq.repo",
				"com.enablix.content.quality.repo",
				"com.enablix.uri.embed.repo"})
public class AnalyticsAppInit {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsAppInit.class, args);
	}
	
}
