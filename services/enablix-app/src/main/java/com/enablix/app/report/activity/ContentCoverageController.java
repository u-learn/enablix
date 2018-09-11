package com.enablix.app.report.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.coverage.ContentCoverageService;
import com.enablix.core.api.ContentTypeCoverageDTO;
import com.enablix.core.api.SearchRequest.Pagination;

@RestController
@RequestMapping("contentcoverage")
public class ContentCoverageController {
	
	@Autowired
	private ContentCoverageService service;
	
	@RequestMapping(path = "/content", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ContentTypeCoverageDTO> getAggregatedActivityMetrices(@RequestBody Pagination pageRequest) {
		return service.getContentTypeCoverage(pageRequest);
	}
}