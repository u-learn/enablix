package com.enablix.task;

import java.util.List;

public interface TaskExecutor {

	void run(List<RunnableTask> tasks);

}