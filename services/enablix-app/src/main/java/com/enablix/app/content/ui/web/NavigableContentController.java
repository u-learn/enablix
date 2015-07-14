package com.enablix.app.content.ui.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.reco.RecommendedContentService;

@RestController
@RequestMapping("navcontent")
public class NavigableContentController {

	@Autowired
	private RecommendedContentService recoService;
	
	@RequestMapping(method = RequestMethod.POST, value="/reco/{containerQId}/")
	public List<NavigableContent> containerRecommendedContent(@PathVariable String containerQId) {
		WebRecommendationRequest request = new WebRecommendationRequest(containerQId);
		return recoService.getRecommendedContent(request);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/reco")
	public List<NavigableContent> generalRecommendedContent() {
		WebRecommendationRequest request = new WebRecommendationRequest();
		return recoService.getRecommendedContent(request);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/recent/{containerQId}")
	public List<NavigableContent> recentlyUpdatedContent(@PathVariable String containerQId) {
		// TODO:
		return new ArrayList<>();
	}
	
}
