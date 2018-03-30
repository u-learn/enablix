package com.enablix.bayes.exec;

import org.springframework.stereotype.Component;

import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class BayesExecTask implements Task {

	@Override
	public void run(TaskContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String taskId() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
