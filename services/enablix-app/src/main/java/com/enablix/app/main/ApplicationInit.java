package com.enablix.app.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import com.enablix.commons.util.StringUtil;

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
		@PropertySource("file:${baseDir}/config/properties/doc-format.properties")})
@ImportResource(locations={"file:${baseDir}/config/integration/*.xml"})
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
				"com.enablix.uri.embed.repo",
				"com.enablix.core.mongo.repo",
				"com.enablix.core.mongo.content.repo",
				"com.enablix.user.task.repo",
				"com.enablix.app.content.pack.repo",
				"com.enablix.ui.widget.repo"})
@EnableScheduling
public class ApplicationInit extends WebMvcAutoConfigurationAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInit.class);
	
	@Value("${baseDir}")
	public String baseDirPath;

	@Value("${ui.assets.external.paths:}")
	private String externalUIAssets;

	@Value("${ui.resource.cache.period:0}")
	public int cachePeriod;
	
	@Value("${ui.cache.resources.flag:false}")
	public boolean cacheResources;
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationInit.class, args);
	}
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		
		final File f = new File(baseDirPath);

		final List<String> resourcesPaths = new ArrayList<>();

		String resourcePath = null;
		
		try {
		
			resourcePath = "file:" + f.getCanonicalPath() + File.separator + AssetUtil.UI_ASSETS_FOLDER_NAME + File.separator;
			resourcesPaths.add(resourcePath);
		
		} catch (final IOException e) {
			LOGGER.info(AssetUtil.UI_ASSETS_FOLDER_NAME + " is missing");
		}

		if (!StringUtil.isEmpty(externalUIAssets)) {

			final String[] paths = externalUIAssets.split(";");

			for (final String path : paths) {
				resourcePath = "file:" + path;
				resourcesPaths.add(resourcePath);
			}
		}

		registry.addResourceHandler("/**")
				.addResourceLocations(resourcesPaths.toArray(new String[resourcesPaths.size()]))
				.setCachePeriod(cachePeriod)
				.resourceChain(false)
				.addResolver(customResourceResolver())
				.addResolver(new DefaultResourceResolver());
	}

	// see https://stackoverflow.com/questions/27381781/java-spring-boot-how-to-map-my-my-app-root-to-index-html
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		 registry.addViewController("/").setViewName("forward:/index.html");
	}
	
	
	@Bean
	public CustomResourceResolver customResourceResolver() {
		
		List<CustomResourcePathBuilder> pathBuilders = new ArrayList<>();
		pathBuilders.add(new ClientBasedResourcePathBuilder());
		pathBuilders.add(new TenantBasedResourcePathBuilder());
		
		return new CustomResourceResolver(pathBuilders);
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

