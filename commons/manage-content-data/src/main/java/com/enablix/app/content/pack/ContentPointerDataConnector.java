package com.enablix.app.content.pack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.content.data.segment.ContentRecordRefDataSegmentConnector;
import com.enablix.app.content.pack.repo.ContentPointerRepository;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.content.pack.ContentPointer;

@Component
public class ContentPointerDataConnector extends ContentRecordRefDataSegmentConnector<ContentPointer> {

	@Autowired
	private ContentPointerRepository repo;
	
	@Override
	public Class<ContentPointer> connectorFor() {
		return ContentPointer.class;
	}

	@Override
	protected ContentRecordRef getContentRecordRef(ContentPointer data) {
		return data.getData();
	}

	@Override
	protected Page<ContentPointer> getNextPageToProcess(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	protected void saveUpdatedPage(Iterable<ContentPointer> page) {
		repo.save(page);
	}

	@Override
	protected List<ContentPointer> getRecordsForIdentity(String contentIdentity) {
		return repo.findByDataInstanceIdentity(contentIdentity);
	}

}
