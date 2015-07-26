package com.enablix.app.content.ui.reco;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.analytics.context.builder.RequestContextBuilder;
import com.enablix.analytics.context.builder.UserContextBuilder;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.builder.RecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.impl.SimpleRecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.analytics.recommendation.impl.PreRecordedRecommendationEngine;
import com.enablix.analytics.web.request.WebRequestContextBuilder;
import com.enablix.analytics.web.request.WebUserContextBuilder;

@Configuration
public class RecommendationConfig {

	@Bean
	public RecommendationEngine preRecordedRecoEngine() {
		return new PreRecordedRecommendationEngine();
	}
	
	@Bean
	public RecommendationContextBuilder<WebRecommendationRequest> recoContextBuilder() {
		return new SimpleRecommendationContextBuilder<WebRecommendationRequest>(
				userRecommendationCtxBuilder(), requestRecommendationCtxBuilder());
	}
	
	@Bean
	public UserContextBuilder<WebRecommendationRequest> userRecommendationCtxBuilder() {
		return new WebUserContextBuilder<WebRecommendationRequest>();
	}
	
	@Bean
	public RequestContextBuilder<WebRecommendationRequest> requestRecommendationCtxBuilder() {
		return new WebRequestContextBuilder<WebRecommendationRequest>();
	}
	
}
