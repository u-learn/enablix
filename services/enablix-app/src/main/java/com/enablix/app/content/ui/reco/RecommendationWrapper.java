package com.enablix.app.content.ui.reco;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.domain.reco.Recommendation;

public class RecommendationWrapper {

	private Recommendation recommendation;
	
	private NavigableContent navContent;
	
	private String clientName;
	
	public Recommendation getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(Recommendation recommendation) {
		this.recommendation = recommendation;
	}

	public NavigableContent getNavContent() {
		return navContent;
	}

	public void setNavContent(NavigableContent navContent) {
		this.navContent = navContent;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}
