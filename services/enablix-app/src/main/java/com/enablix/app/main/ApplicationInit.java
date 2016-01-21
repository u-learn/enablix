package com.enablix.app.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAutoConfiguration(exclude={ElasticsearchDataAutoConfiguration.class, ElasticsearchAutoConfiguration.class})
@Configuration
@PropertySources({
		@PropertySource("file:${baseDir}/config/properties/mongodb.properties"),
		@PropertySource("file:${baseDir}/config/properties/app.properties"),
		@PropertySource("file:${baseDir}/config/properties/security.properties"),
		@PropertySource("file:${baseDir}/config/properties/elasticsearch.properties"),
		@PropertySource("file:${baseDir}/config/properties/log4j.properties")})
@ComponentScan(basePackages = { "com.enablix" })
@EnableMongoRepositories(
		basePackages = {"com.enablix.app.mongo.repository", 
				"com.enablix.commons.dms.repository",
				"com.enablix.core.security.auth.repo",
				"com.enablix.analytics.recommendation.repository", 
				"com.enablix.core.mongo.config.repo",
				"com.enablix.app.content.recent.repo",
				"com.enablix.app.content.ui.link.repo"})
public class ApplicationInit {

	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationInit.class, args);
	}
	
}

/*@Component
class SaveUser {
	
	@Autowired
	private UserRepository userRepo;
	
	@PostConstruct
	private void saveUser() {
		User user = new User();
		user.setUserId("user1234");
		user.setTenantId("test");
		user.setIdentity("user1234");
		user.setPassword("password");
		
		userRepo.save(user);
	}
}*/

/*@Component
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
	
}*/

