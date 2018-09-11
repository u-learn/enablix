package com.enablix.app.content.coverage;

import org.springframework.data.domain.Page;

import com.enablix.core.api.ContentTypeCoverageDTO;
import com.enablix.core.api.SearchRequest.Pagination;

public interface ContentCoverageService {

	Page<ContentTypeCoverageDTO> getContentTypeCoverage(Pagination pagination);
	
}
