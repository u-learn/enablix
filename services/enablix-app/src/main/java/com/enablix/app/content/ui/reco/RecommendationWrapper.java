package com.enablix.app.content.ui.reco;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.domain.reco.Recommendation;

public class RecommendationWrapper {

	private Recommendation recommendation;
	
	private NavigableContent navContent;
	
	private String clientName;
	
	private int order;
	
	private String identity;
	
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
}
