package com.enablix.app.main;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.reco.RecommendationScope;
import com.enablix.core.domain.reco.RecommendedData;
import com.enablix.core.domain.reco.Recommendation;

@EnableAutoConfiguration
@Configuration
@PropertySources({
		@PropertySource("file:${baseDir}/config/properties/mongodb.properties"),
		@PropertySource("file:${baseDir}/config/properties/app.properties"),
		@PropertySource("file:${baseDir}/config/properties/security.properties"),
		@PropertySource("file:${baseDir}/config/properties/log4j.properties")})
@ComponentScan(basePackages = { "com.enablix" })
@EnableMongoRepositories(
		basePackages = {"com.enablix.app.mongo.repository", 
				"com.enablix.commons.dms.repository",
				"com.enablix.analytics.recommendation.repository", 
				"com.enablix.core.mongo.config.repo"})
public class ApplicationInit {

	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationInit.class, args);
	}
	
}

@Component
class Temp {
	
	@Autowired
	RecommendationRepository userRecoRepo;
	
	@PostConstruct
	public void saveUserReco() {
		
		ProcessContext.initialize("system", "test", "enterprise-software-template");
		
		Recommendation reco = new Recommendation();
		
		RecommendationScope recoScope = new RecommendationScope();
		recoScope.setUserId("userId");
		recoScope.setTemplateId("enterprise-software-template");
		recoScope.setContainerQId("solution");
		recoScope.setContentIdentity("0abb1cd5-22a6-46bf-9af2-3949f99ba91e");
		
		reco.setRecommendationScope(recoScope);
		
		RecommendedData recoData = new RecommendedData();
		
		ContentDataRef refData = new ContentDataRef(
				"enterprise-software-template", "solution", 
				"09083139-6d13-4a2f-92c1-ed4b82908725");
		
		recoData.setData(refData );
		
		reco.setRecommendedData(recoData);
		
		userRecoRepo.save(reco);
		
		ProcessContext.clear();
		
	}
	
}

