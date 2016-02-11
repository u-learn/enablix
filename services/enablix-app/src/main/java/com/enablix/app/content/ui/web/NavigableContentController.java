package com.enablix.app.content.ui.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.link.QuickLinks;
import com.enablix.app.content.ui.link.QuickLinksService;
import com.enablix.app.content.ui.peers.PeerContentService;
import com.enablix.app.content.ui.recent.RecentContentService;
import com.enablix.app.content.ui.reco.RecommendedContentService;

@RestController
@RequestMapping("navcontent")
public class NavigableContentController {

	@Autowired
	private RecommendedContentService recoService;
	
	@Autowired
	private RecentContentService recentService;
	
	@Autowired
	private QuickLinksService quickLinksService;
	
	@Autowired
	private PeerContentService peerService;
	
	@RequestMapping(method = RequestMethod.GET, value="/reco/{containerQId}/")
	public List<NavigableContent> containerRecommendedContent(@PathVariable String containerQId) {
		WebRecommendationRequest request = new WebRecommendationRequest(containerQId);
		return recoService.getRecommendedContent(request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reco")
	public List<NavigableContent> generalRecommendedContent() {
		WebRecommendationRequest request = new WebRecommendationRequest();
		return recoService.getRecommendedContent(request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reco/{containerQId}/{contentIdentity}/")
	public List<NavigableContent> contentSpecificRecommendedContent(@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		WebRecommendationRequest request = new WebRecommendationRequest(containerQId, contentIdentity);
		return recoService.getRecommendedContent(request);
	}

	@RequestMapping(method = RequestMethod.GET, value="/recent/{containerQId}/")
	public List<NavigableContent> containerRecentContent(@PathVariable String containerQId) {
		WebContentRequest request = new WebContentRequest(containerQId);
		return recentService.getRecentContent(request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/recent/")
	public List<NavigableContent> generalRecentContent() {
		WebContentRequest request = new WebContentRequest();
		return recentService.getRecentContent(request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/recent/{containerQId}/{contentIdentity}/")
	public List<NavigableContent> contentSpecificRecentContent(
			@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		
		WebContentRequest request = new WebContentRequest(containerQId, contentIdentity);
		return recentService.getRecentContent(request);
	}

	@RequestMapping(method = RequestMethod.GET, value="/quicklinks/")
	public QuickLinks quickLinks() {
		return quickLinksService.getQuickLinks();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/peers/{containerQId}/{contentIdentity}/")
	public List<NavigableContent> fetchPeers(
			@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		
		WebContentRequest request = new WebContentRequest(containerQId, contentIdentity);
		return peerService.getPeers(request);
	}
	
}
