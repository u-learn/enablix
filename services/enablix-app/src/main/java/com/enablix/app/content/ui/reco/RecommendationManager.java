package com.enablix.app.content.ui.reco;

import java.util.Collection;

import com.enablix.core.domain.reco.Recommendation;

public interface RecommendationManager {

	public void saveRecommendation(Recommendation reco);
	
	public void deleteRecommendation(String recommendationIdentity);
	
	public Collection<RecommendationWrapper> getAllGenericRecommendations();
	
}
