package com.enablix.app.content.recent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.content.data.segment.ContentRecordRefDataSegmentConnector;
import com.enablix.app.content.recent.repo.RecentDataRepository;
import com.enablix.core.api.ContentRecord;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.domain.segment.DataSegmentInfo;

@Component
public class RecentContentDataSegmentConnector extends ContentRecordRefDataSegmentConnector<RecentData> {

	@Autowired
	private RecentDataRepository repo;
	
	@Override
	public Class<RecentData> connectorFor() {
		return RecentData.class;
	}

	@Override
	protected ContentRecordRef getContentRecordRef(RecentData data) {
		return data.getData();
	}

	@Override
	protected Page<RecentData> getNextPageToProcess(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	protected void saveUpdatedPage(Iterable<RecentData> page) {
		repo.save(page);
	}

	@Override
	protected List<RecentData> getRecordsForIdentity(String contentIdentity) {
		return repo.findByDataInstanceIdentity(contentIdentity);
	}
	
	@Override
	public void updateRecord(ContentRecord updatedContent, DataSegmentInfo dataSegmentInfo) {
		// DO nothing. The records are updated by RecentDataCollector
	}

}
