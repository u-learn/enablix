package com.enablix.core.domain.reco;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.DataSegmentAwareEntity;

@Document(collection = "ebx_recommendation")
public class Recommendation extends DataSegmentAwareEntity {

	private RecommendationScope recommendationScope;
	
	private RecommendedData recommendedData;

	public RecommendationScope getRecommendationScope() {
		return recommendationScope;
	}

	public void setRecommendationScope(RecommendationScope recommendationFactors) {
		this.recommendationScope = recommendationFactors;
	}

	public RecommendedData getRecommendedData() {
		return recommendedData;
	}

	public void setRecommendedData(RecommendedData recommendedData) {
		this.recommendedData = recommendedData;
	}

}
