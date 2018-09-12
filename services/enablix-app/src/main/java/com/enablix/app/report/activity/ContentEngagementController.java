package com.enablix.app.report.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.engagement.ContentEngagementService;
import com.enablix.core.api.ContentEngagementDTO;
import com.enablix.core.api.ContentEngagementRequest;

@RestController
@RequestMapping("enggrpt")
public class ContentEngagementController {
	
	@Autowired
	private ContentEngagementService service;
	
	@RequestMapping(path = "/content", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ContentEngagementDTO> getContentEngagement(@RequestBody ContentEngagementRequest request) {
		return service.getContentEngagement(request);
	}
}