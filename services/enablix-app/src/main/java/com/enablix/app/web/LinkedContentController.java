package com.enablix.app.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.LinkedContent;
import com.enablix.core.api.LinkedContentRequest;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.hubspot.model.HubspotCRMExtResponse;
import com.enablix.hubspot.model.HubspotContent;

@RestController
public class LinkedContentController {

	@Autowired
	private LinkedContentService linkedContentService;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{tenantId}/hbspt/content/linked/{refQId}/{refMatchAttrId}/{refMatchAttrValue}/{lookupContentQId}/", 
			produces = "application/json")
	public HubspotCRMExtResponse hubspotContentRequest(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @PathVariable String refQId,
			@PathVariable String refMatchAttrId, @PathVariable String refMatchAttrValue,
			@PathVariable String lookupContentQId,
			@RequestParam String userEmail) {
		
		List<LinkedContent<ContentDataRecord>> content = 
				linkedContentService.fetchLinkedContentData(refQId, refMatchAttrId, 
						refMatchAttrValue, lookupContentQId, null);
		
		long objectId = 0;
		
		List<HubspotContent> hbContents = new ArrayList<>();
		
		if (content != null) {
			
			for (LinkedContent<ContentDataRecord> ct : content) {
				
				HubspotContent hbContent = new HubspotContent();
				hbContent.setObjectId(++objectId);
				hbContent.setTitle(ct.getContentLabel());
				
				hbContents.add(hbContent);
			}
		}
		
		HubspotCRMExtResponse hbResponse = new HubspotCRMExtResponse();
		hbResponse.setResults(hbContents);
		hbResponse.setTotalCount(hbResponse.getResults().size());
		hbResponse.setItemLabel("See more content");
		
		return hbResponse;
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{tenantId}/hbspt/testcontent/", 
			produces = "application/json")
	public Map<String, Object> hubspotTestContentRequest(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestParam String userEmail) {
		
		Configuration config = ConfigurationUtil.getConfig("HUBSPOT_TEST_CONTENT");
		return config.getConfig();
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/content/linked/", consumes = "application/json",
			produces = "application/json")
	public List<LinkedContent<DisplayableContent>> getSpecificLinkedContent(
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody LinkedContentRequest contentRequest,
			@RequestHeader(value="requestorId", required=false) String userEmailId) {
		
		return linkedContentService.fetchDisplayableLinkedContent(contentRequest.getRefContentQId(), 
				contentRequest.getRefMatchAttrId(), contentRequest.getRefMatchAttrValue(), 
				contentRequest.getLookupContentQId(), 
				contentRequest.getContentBusinessCategory(), userEmailId);
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/content/linked/mapped/", consumes = "application/json", 
			produces = "application/json")
	public List<DisplayableContent> getCategoryMappedContent(
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody LinkedContentRequest contentRequest,
			@RequestHeader(value="requestorId", required=false) String userEmailId) {

		return linkedContentService.getLinkedAndMappedDisplayableContent(contentRequest, userEmailId);
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/data/linked/mapped/", consumes = "application/json", 
			produces = "application/json")
	public List<ContentDataRecord> getLinkedAndMappedContentData(@RequestBody LinkedContentRequest contentRequest) {
		return linkedContentService.getLinkedAndMappedContentData(contentRequest);
	}

}
