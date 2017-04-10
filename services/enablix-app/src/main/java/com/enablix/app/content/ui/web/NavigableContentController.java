package com.enablix.app.content.ui.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.recent.RecentContentService;
import com.enablix.app.content.recent.RecentUpdateVO;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.nav.NavigationPathService;
import com.enablix.app.content.ui.peers.PeerContentService;
import com.enablix.app.content.ui.reco.RecommendedContentService;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;

@RestController
@RequestMapping("navcontent")
public class NavigableContentController {

	@Autowired
	private RecommendedContentService recoService;
	
	@Autowired
	private RecentContentService recentService;
	
	@Autowired
	private PeerContentService peerService;
	
	@Autowired
	private NavigationPathService navPathService;

	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.GET, value="/reco/{containerQId}/")
	public List<NavigableContent> containerRecommendedContent(@PathVariable String containerQId) {
		WebRecommendationRequest request = new WebRecommendationRequest(containerQId);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recoService.getRecommendedContent(request, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reco")
	public List<NavigableContent> generalRecommendedContent() {
		WebRecommendationRequest request = new WebRecommendationRequest();
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recoService.getRecommendedContent(request, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reco/{containerQId}/{contentIdentity}/")
	public List<NavigableContent> contentSpecificRecommendedContent(@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		WebRecommendationRequest request = new WebRecommendationRequest(containerQId, contentIdentity);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recoService.getRecommendedContent(request, userDataView);
	}

	@RequestMapping(method = RequestMethod.GET, value="/recent/{containerQId}/")
	public List<NavigableContent> containerRecentContent(@PathVariable String containerQId) {
		WebContentRequest request = new WebContentRequest(containerQId);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recentService.getRecentContent(request, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/recent/")
	public List<NavigableContent> generalRecentContent() {
		WebContentRequest request = new WebContentRequest();
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recentService.getRecentContent(request, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/recent/{containerQId}/{contentIdentity}/")
	public List<NavigableContent> contentSpecificRecentContent(
			@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		
		WebContentRequest request = new WebContentRequest(containerQId, contentIdentity);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recentService.getRecentContent(request, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/recentlist")
	public Page<RecentUpdateVO> fetchRecentUpdateList(@RequestBody SearchRequest searchRequest) {
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return recentService.getRecentContentByRequest(searchRequest, userDataView);
	}

	@RequestMapping(method = RequestMethod.GET, value="/peers/{containerQId}/{contentIdentity}/")
	public List<NavigableContent> fetchPeers(
			@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		
		WebContentRequest request = new WebContentRequest(containerQId, contentIdentity);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return peerService.getPeers(request, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/navpath/{containerQId}/{contentIdentity}/")
	public NavigableContent navigationPath(
			@PathVariable String containerQId, 
			@PathVariable String contentIdentity) {
		
		return navPathService.getNavPath(containerQId, contentIdentity);
	}
	
}
