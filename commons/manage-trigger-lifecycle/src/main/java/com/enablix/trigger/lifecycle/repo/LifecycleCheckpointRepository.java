package com.enablix.trigger.lifecycle.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.LifecycleCheckpoint.ExecutionStatus;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@SuppressWarnings("rawtypes")
public interface LifecycleCheckpointRepository extends BaseMongoRepository<LifecycleCheckpoint> {

	List<LifecycleCheckpoint> findByStatusAndScheduledExecDateBefore(ExecutionStatus status, Date scheduledExecDate);
	
	@Query("{ $and : [{'status' : ?0}, {'trigger.triggerItem.instanceIdentity' : ?1},"
			+ " {$or: [{'checkpointDefinition.executionTime.resetOn' : {$exists : true} }, "
				   + "{'checkpointDefinition.executionTime.disableOn' : {$exists : true} }] } ]}")
	List<LifecycleCheckpoint<?>> findByStatusAndTriggerItem(ExecutionStatus status, String instanceIdentity);
	
}

