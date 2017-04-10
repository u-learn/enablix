package com.enablix.app.content.ui.reco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.app.content.data.segment.ContentRecordRefDataSegmentConnector;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.reco.Recommendation;

@Component
public class RecoDataSegmentConnector extends ContentRecordRefDataSegmentConnector<Recommendation> {

	@Autowired RecommendationRepository repo;
	
	@Override
	public Class<Recommendation> connectorFor() {
		return Recommendation.class;
	}

	@Override
	protected ContentRecordRef getContentRecordRef(Recommendation data) {
		return data.getRecommendedData().getData();
	}

	@Override
	protected Page<Recommendation> getNextPageToProcess(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	protected void saveUpdatedPage(Page<Recommendation> page) {
		repo.save(page);
	}
	
}
