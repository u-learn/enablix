package com.enablix.app.content.pack.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enablix.core.domain.content.pack.ContentPointer;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ContentPointerRepository extends BaseMongoRepository<ContentPointer> {

	List<ContentPointer> findByDataInstanceIdentity(String identity);

	Page<ContentPointer> findByParentIdentity(String identity, Pageable pageable);

	void deleteByParentIdentity(String identity);
	
}
