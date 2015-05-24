package com.enablix.core.domain.reco;

import com.enablix.core.domain.BaseDocumentEntity;

public class UserRecommendation extends BaseDocumentEntity {

	private String userId;
	
	private RecommendedData recommendedData;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public RecommendedData getRecommendedData() {
		return recommendedData;
	}

	public void setRecommendedData(RecommendedData recommendedData) {
		this.recommendedData = recommendedData;
	}

}
