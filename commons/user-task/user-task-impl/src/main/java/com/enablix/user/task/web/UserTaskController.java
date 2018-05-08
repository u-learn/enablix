package com.enablix.user.task.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.user.task.UserTask;
import com.enablix.user.task.UserTaskManager;

@RestController
@RequestMapping("usertask")
public class UserTaskController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskController.class);

	@Autowired
	private UserTaskManager taskManager;
	
	@RequestMapping(value = "/incmplt", method = RequestMethod.GET, produces = "application/json")
	public List<UserTask> getUserTasks() {
		LOGGER.debug("Fetching user tasks");
		return taskManager.getUserTasks(ProcessContext.get().getUserId());	
	}
	
	@RequestMapping(value = "/updtmilestone", method = RequestMethod.POST, 
			produces = "application/json", consumes = "application/json")
	public UserTask updateMilestone(@RequestBody UpdateMilestoneRequest request) {
		return taskManager.updateMilestone(request.getTaskIdentity(), 
				request.getStepId(), request.getMilestone());	
	}
	

	
	
}