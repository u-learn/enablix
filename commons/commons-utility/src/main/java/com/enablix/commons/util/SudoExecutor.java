package com.enablix.commons.util;

import com.enablix.commons.util.process.ProcessContext;

public class SudoExecutor {

	public static void runAsUser(String userId, String userDisplayName, Task task) {
		
		ProcessContext origCtx = ProcessContext.get();
		
		try {
			ProcessContext.clear();
			ProcessContext.initialize(userId, userDisplayName, origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
			
			task.execute();
			
		} finally {

			ProcessContext.clear();
			ProcessContext.initialize(origCtx.getUserId(), origCtx.getUserDisplayName(), origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
		}
	}
	
	public interface Task {
		void execute();
	}
	
}
