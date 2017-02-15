package com.enablix.app.content.kit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.kit.repo.ContentKitRepository;
import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.core.domain.content.kit.ContentKit;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
public class ContentKitCrudService extends MongoRepoCrudService<ContentKit> {

	@Autowired
	private ContentKitRepository repo;

	@Override
	public BaseMongoRepository<ContentKit> getRepository() {
		return repo;
	}

	@Override
	protected ContentKit merge(ContentKit t, ContentKit existing) {
		BeanUtil.copyBusinessAttributes(t, existing);
		return existing;
	}
	
	
}
