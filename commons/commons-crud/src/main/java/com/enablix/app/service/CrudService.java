package com.enablix.app.service;

import com.enablix.core.domain.BaseEntity;

public interface CrudService<T extends BaseEntity> {

	CrudResponse<T> saveOrUpdate(T t);
	
	T findByIdentity(String identity);
	
}
