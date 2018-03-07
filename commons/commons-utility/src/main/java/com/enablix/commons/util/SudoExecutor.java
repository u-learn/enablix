package com.enablix.commons.util;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;

public class SudoExecutor {

	public static void runAsUser(String userId, String userDisplayName, Task task) {
		
		ProcessContext origCtx = ProcessContext.get();
		
		try {
			ProcessContext.clear();
			
			ProcessContext.initialize(userId, userDisplayName, 
					origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
			
			task.execute();
			
		} finally {

			ProcessContext.clear();
			
			ProcessContext.initialize(origCtx.getUserId(), origCtx.getUserDisplayName(), 
					origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
		}
	}
	
	public static void runAsSystem(Task task) {
		
		ProcessContext origCtx = ProcessContext.get();
		
		try {
			
			ProcessContext.clear();
			
			ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, AppConstants.SYSTEM_USER_NAME, 
					origCtx != null ? origCtx.getTenantId() : null, 
					origCtx != null ? origCtx.getTemplateId() : null, 
					origCtx != null ? origCtx.getClientId() : null);
			
			task.execute();
			
		} finally {

			ProcessContext.clear();
			
			if (origCtx != null) {
				ProcessContext.initialize(origCtx.getUserId(), origCtx.getUserDisplayName(), 
						origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
			}
		}
		
	}
	
	public interface Task {
		void execute();
	}
	
}
