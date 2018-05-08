package com.enablix.user.task;

import java.util.List;

import com.enablix.core.domain.user.task.UserTask;

public interface UserTaskManager {

	List<UserTask> getUserTasks(String userId);
	
	UserTask updateMilestone(String taskIdentity, String stepId, String milestone);
	
}
