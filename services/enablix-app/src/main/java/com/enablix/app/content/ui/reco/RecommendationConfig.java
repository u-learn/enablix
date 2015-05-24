package com.enablix.app.content.ui.reco;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.builder.RecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.RequestContextBuilder;
import com.enablix.analytics.recommendation.builder.UserContextBuilder;
import com.enablix.analytics.recommendation.builder.impl.SimpleRecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.analytics.recommendation.builder.web.WebRequestContextBuilder;
import com.enablix.analytics.recommendation.builder.web.WebUserContextBuilder;
import com.enablix.analytics.recommendation.impl.PreRecordedRecommendationEngine;

@Configuration
public class RecommendationConfig {

	@Bean
	public RecommendationEngine preRecordedRecoEngine() {
		return new PreRecordedRecommendationEngine();
	}
	
	@Bean
	public RecommendationContextBuilder<WebRecommendationRequest> recoContextBuilder() {
		return new SimpleRecommendationContextBuilder<WebRecommendationRequest>(
				userCtxBuilder(), requestCtxBuilder());
	}
	
	@Bean
	public UserContextBuilder<WebRecommendationRequest> userCtxBuilder() {
		return new WebUserContextBuilder();
	}
	
	@Bean
	public RequestContextBuilder<WebRecommendationRequest> requestCtxBuilder() {
		return new WebRequestContextBuilder();
	}
	
}
