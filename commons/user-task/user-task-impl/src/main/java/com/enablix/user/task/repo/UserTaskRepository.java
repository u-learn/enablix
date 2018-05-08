package com.enablix.user.task.repo;

import java.util.List;

import com.enablix.core.domain.user.task.TaskStatus;
import com.enablix.core.domain.user.task.UserTask;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserTaskRepository extends BaseMongoRepository<UserTask> {

	List<UserTask> findByUserId(String userId);

	List<UserTask> findByUserIdAndStatusNot(String userId, TaskStatus status);

}
