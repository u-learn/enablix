package com.enablix.user.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.user.task.TaskStatus;
import com.enablix.core.domain.user.task.UserTask;
import com.enablix.core.domain.user.task.UserTaskStep;
import com.enablix.user.task.repo.UserTaskRepository;

@Component
public class UserTaskManagerImpl implements UserTaskManager {

	@Autowired
	private UserTaskRepository userTaskRepo;
	
	@Override
	public List<UserTask> getUserTasks(String userId) {
		return userTaskRepo.findByUserIdAndStatusNot(userId, TaskStatus.COMPLETE);
	}

	@Override
	public UserTask updateMilestone(String taskIdentity, String stepId, String milestone) {
		
		UserTask task = userTaskRepo.findByIdentity(taskIdentity);
		
		if (task != null) {
		
			UserTaskStep step = task.getStep(stepId);
			
			if (step != null && !step.getMilestonesCovered().contains(milestone)) {
				
				step.getMilestonesCovered().add(milestone);
				
				if (milestone.equals(step.getFinalMilestone())) {
					step.setStatus(TaskStatus.COMPLETE);
				} else if (step.getStatus() == TaskStatus.PENDING) {
					step.setStatus(TaskStatus.IN_PROGRESS);
				}
				
				updateTaskStatus(task);
				
				userTaskRepo.save(task);
			}
		}
		
		return task;
	}

	private void updateTaskStatus(UserTask task) {
		
		TaskStatus status = TaskStatus.COMPLETE;
		
		for (UserTaskStep step : task.getSteps()) {
			
			if (step.getStatus() != TaskStatus.COMPLETE) {
				status = TaskStatus.IN_PROGRESS;
				break;
			}
		}
		
		task.setStatus(status);
	}

}
