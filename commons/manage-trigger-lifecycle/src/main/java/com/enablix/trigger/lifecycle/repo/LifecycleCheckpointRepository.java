package com.enablix.trigger.lifecycle.repo;

import java.util.Date;
import java.util.List;

import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.LifecycleCheckpoint.ExecutionStatus;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@SuppressWarnings("rawtypes")
public interface LifecycleCheckpointRepository extends BaseMongoRepository<LifecycleCheckpoint> {

	List<LifecycleCheckpoint> findByStatusAndScheduledExecDateBefore(ExecutionStatus status, Date scheduledExecDate);
	
}

