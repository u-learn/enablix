package com.enablix.app.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentService;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.ContentRecordGroup;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.hubspot.DisplayableToHubspotContentTx;
import com.enablix.hubspot.model.HubspotCRMExtResponse;
import com.enablix.hubspot.model.HubspotConstants;
import com.enablix.hubspot.model.HubspotContent;
import com.enablix.hubspot.model.HubspotContentKitConfig;

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
	private DisplayableContentService dsplyContentService;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
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
		
		return createHubspotResponse(hbContentList);
	}

	private HubspotCRMExtResponse createHubspotResponse(List<HubspotContent> hbContentList) {
		HubspotCRMExtResponse hbResponse = new HubspotCRMExtResponse();
		hbResponse.setResults(hbContentList);
		hbResponse.setTotalCount(hbResponse.getResults().size());
		return hbResponse;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{tenantId}/hbspt/ck/{matchAttr}", 
			produces = "application/json")
	public HubspotCRMExtResponse hubspotContentKit(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @PathVariable String matchAttr,
			@RequestParam String userEmail) {
		
		String containerQId = null;
		String recordIdentity = null;
		
		Configuration config = ConfigurationUtil.getConfig(HubspotConstants.HUBSPOT_CONFIG_KEY);
		
		if (config != null) {
		
			Object configVal = config.getConfig().get(HubspotConstants.HUBSPOT_CK_CONFIG_KEY);
			
			if (configVal != null && configVal instanceof Collection) {
				
				Map<String, String> matchAttrs = new HashMap<>();
				matchAttrs.put(matchAttr, request.getParameter(matchAttr));
				
				Collection ckConfigList = (Collection) configVal;
				
				for (Object ckConfig : ckConfigList) {
					
					if (ckConfig instanceof HubspotContentKitConfig) {
						
						HubspotContentKitConfig hbCkConfig = (HubspotContentKitConfig) ckConfig;
						
						if (hbCkConfig.matches(matchAttrs)) {
							containerQId = hbCkConfig.getContainerQId();
							recordIdentity = hbCkConfig.getRecordIdentity();
							break;
						}
						
					}
				}
			}
		}
		
		List<HubspotContent> hbContentList = new ArrayList<>();
		
		if (StringUtil.hasText(containerQId) && StringUtil.hasText(recordIdentity)) {
		
			DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			List<ContentRecordGroup> contentStackRecords = 
					contentDataMgr.getContentStackForContentRecord(containerQId, recordIdentity, null, dataView);
			
			DisplayContext ctx = hubspotDisplayContext();
			
			if (contentStackRecords != null) {
				
				long objectId = 0;
				
				for (ContentRecordGroup crg : contentStackRecords) {
					
					for (Map<String, Object> record : crg.getRecords().getContent()) {
						DisplayableContent dc = dsplyContentService.convertToDisplayableContent(crg.getContentQId(), record, ctx);
						hbContentList.add(createHubspotContent(template, userEmail, ++objectId, dc, ctx));
					}
				}
			}
		}
		
		return createHubspotResponse(hbContentList);
	}

	private DisplayContext hubspotDisplayContext() {
		
		DisplayContext ctx = new DisplayContext();
		ctx.setDisplayChannel(Channel.HUBSPOT);
		
		return ctx;
	}
	
	private HubspotContent getHubspotContent(ContentDataRef contentRef, 
			DataView view, TemplateFacade template, String userEmailId, long hbObjectId) {
		
		DisplayContext ctx = hubspotDisplayContext();
		
		DisplayableContent dispRecord = dsplyContentService.getDisplayableContent(
				contentRef.getContentQId(), contentRef.getInstanceIdentity(), view, ctx);
		
		return createHubspotContent(template, userEmailId, hbObjectId, dispRecord, ctx);
	}

	private HubspotContent createHubspotContent(TemplateFacade template, String userEmailId, long hbObjectId,
			DisplayableContent dispRecord, DisplayContext ctx) {
		
		docUrlPopulator.populateUnsecureUrl(dispRecord, userEmailId, ctx);
		textLinkProcessor.process(dispRecord, template, userEmailId, ctx);
		
		return hbContentTx.transform(dispRecord, hbObjectId);
	}
		
}
