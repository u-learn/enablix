package com.enablix.app.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.builder.RecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayableContentService;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.hubspot.DisplayableToHubspotContentTx;
import com.enablix.hubspot.model.HubspotCRMExtResponse;
import com.enablix.hubspot.model.HubspotContent;

@RestController
public class HubspotController {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Autowired
	private TextLinkProcessor textLinkProcessor;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Autowired
	private RecommendationEngine recoEngine;
	
	@Autowired
	private RecommendationContextBuilder<WebRecommendationRequest> recoCtxBuilder;
	
	@Autowired
	private DisplayableToHubspotContentTx hbContentTx;
	
	@Autowired
	private DisplayableContentService contentService;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{tenantId}/hbspt/recolist/", 
			produces = "application/json")
	public HubspotCRMExtResponse hubspotTestContentRequest(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestParam String userEmail) {
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());

		WebRecommendationRequest recoRequest = new WebRecommendationRequest();
		RecommendationContext recoCtx = recoCtxBuilder.build(recoRequest);

		List<ContentDataRef> recommendations = recoEngine.getRecommendations(recoCtx, dataView);
		
		List<HubspotContent> hbContentList = new ArrayList<>();
		
		long objectId = 0;
		
		for (ContentDataRef contentRef : recommendations) {
			
			HubspotContent hubspotContent = getHubspotContent(contentRef, dataView, template, userEmail, ++objectId);
			if (hubspotContent != null) {
				hbContentList.add(hubspotContent);
			}
		}
		
		HubspotCRMExtResponse hbResponse = new HubspotCRMExtResponse();
		hbResponse.setResults(hbContentList);
		hbResponse.setTotalCount(hbResponse.getResults().size());
	
		return hbResponse;
	}
	
	private HubspotContent getHubspotContent(ContentDataRef contentRef, 
			DataView view, TemplateFacade template, String userEmailId, long hbObjectId) {
		
		DisplayableContent dispRecord = contentService.getDisplayableContent(
				contentRef.getContentQId(), contentRef.getInstanceIdentity(), view);
		
		docUrlPopulator.populateUnsecureUrl(dispRecord, userEmailId);
		textLinkProcessor.process(dispRecord, template, userEmailId);
		
		return hbContentTx.transform(dispRecord, hbObjectId);
	}
		
}
