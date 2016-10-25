package com.enablix.state.change.repo;

import org.springframework.data.repository.NoRepositoryBean;

import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

@NoRepositoryBean
public interface StateChangeRecordingRepository<O extends RefObject, T extends StateChangeRecording<O>> extends BaseMongoRepository<T> {

	T findByObjectRefIdentity(String refObjectIdentity);
	
	Long deleteByObjectRefIdentity(String refObjectIdentity);
	
}
