package com.enablix.core.mongo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.enablix.core.domain.BaseEntity;

@NoRepositoryBean
public interface BaseMongoRepository<T extends BaseEntity> extends MongoRepository<T, String> {

	T findByIdentity(String identity);
	
	Long deleteByIdentity(String identity);
	
	List<T> removeByIdentity(String identity);
	
	List<T> findByIdentityIn(Collection<String> identity);
	
}
