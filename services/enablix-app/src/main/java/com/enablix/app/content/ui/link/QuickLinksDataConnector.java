package com.enablix.app.content.ui.link;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.content.data.segment.ContentRecordRefDataSegmentConnector;
import com.enablix.app.content.ui.link.repo.QuickLinkContentRepository;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.links.QuickLinkContent;

@Component
public class QuickLinksDataConnector extends ContentRecordRefDataSegmentConnector<QuickLinkContent> {

	@Autowired
	private QuickLinkContentRepository repo;
	
	@Override
	public Class<QuickLinkContent> connectorFor() {
		return QuickLinkContent.class;
	}

	@Override
	protected ContentRecordRef getContentRecordRef(QuickLinkContent data) {
		return data.getData();
	}

	@Override
	protected Page<QuickLinkContent> getNextPageToProcess(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	protected void saveUpdatedPage(Iterable<QuickLinkContent> page) {
		repo.save(page);
	}

	@Override
	protected List<QuickLinkContent> getRecordsForIdentity(String contentIdentity) {
		return repo.findByDataInstanceIdentity(contentIdentity);
	}

}
