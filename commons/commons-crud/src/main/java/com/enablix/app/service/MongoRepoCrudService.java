package com.enablix.app.service;

import com.enablix.core.domain.BaseEntity;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public abstract class MongoRepoCrudService<T extends BaseEntity> extends AbstractCrudService<T> {

	@Override
	public T doSave(T t) {
		t = getRepository().save(t);
		return t;
	}

	@Override
	public T findExisting(T t) {
		return getRepository().findByIdentity(t.getIdentity());
	}

	@Override
	public T findByIdentity(String identity) {
		return getRepository().findByIdentity(identity);
	}
	
	public abstract BaseMongoRepository<T> getRepository();
	
}
