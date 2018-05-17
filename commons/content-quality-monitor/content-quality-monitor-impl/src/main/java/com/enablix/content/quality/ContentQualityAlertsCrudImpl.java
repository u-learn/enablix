package com.enablix.content.quality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.content.quality.repo.ContentQualityAlertsRepository;
import com.enablix.core.domain.content.quality.ContentQualityAnalysis;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
@Deprecated
public class ContentQualityAlertsCrudImpl extends MongoRepoCrudService<ContentQualityAnalysis> implements ContentQualityAlertsCrud {

	@Autowired
	private ContentQualityAlertsRepository qualityAlertsRepo;
	
	@Override
	public BaseMongoRepository<ContentQualityAnalysis> getRepository() {
		return qualityAlertsRepo;
	}

	@Override
	public ContentQualityAnalysis findExisting(ContentQualityAnalysis t) {
		return qualityAlertsRepo.findByContentIdentity(t.getContentIdentity());
	}
	
	@Override
	protected ContentQualityAnalysis merge(ContentQualityAnalysis t, ContentQualityAnalysis existing) {
		existing.setAnalysis(t.getAnalysis());
		return existing;
	}

	@Override
	public void deleteByContentIdentity(String contentIdentity) {
		qualityAlertsRepo.deleteByContentIdentity(contentIdentity);
	}

}
