package com.enablix.app.content.engagement;

import org.springframework.data.domain.Page;

import com.enablix.core.api.ContentEngagementDTO;
import com.enablix.core.api.ContentEngagementRequest;

public interface ContentEngagementService {

	Page<ContentEngagementDTO> getContentEngagement(ContentEngagementRequest request);

}
