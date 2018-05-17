package com.enablix.content.quality.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.content.data.segment.ContentRecordRefDataSegmentConnector;
import com.enablix.content.quality.repo.ContentQualityAlertRepository;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.content.quality.ContentQualityAlert;

@Component
public class ContentQualityAlertSegmentConnector extends ContentRecordRefDataSegmentConnector<ContentQualityAlert> {

	@Autowired 
	private ContentQualityAlertRepository repo;
	
	@Override
	public Class<ContentQualityAlert> connectorFor() {
		return ContentQualityAlert.class;
	}

	@Override
	protected ContentRecordRef getContentRecordRef(ContentQualityAlert data) {
		return data;
	}

	@Override
	protected Page<ContentQualityAlert> getNextPageToProcess(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	protected void saveUpdatedPage(Iterable<ContentQualityAlert> page) {
		repo.save(page);
	}

	@Override
	protected List<ContentQualityAlert> getRecordsForIdentity(String contentIdentity) {
		return repo.findByRecordIdentity(contentIdentity);
	}
	
}
