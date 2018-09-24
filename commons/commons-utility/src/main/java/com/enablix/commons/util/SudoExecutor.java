package com.enablix.commons.util;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;

public class SudoExecutor {

	public static <T> T runAsUser(String userId, String userDisplayName, Task<T> task) {
		
		T result = null;
		ProcessContext origCtx = ProcessContext.get();
		
		try {
			ProcessContext.clear();
			
			ProcessContext.initialize(userId, userDisplayName, 
					origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
			
			result = task.execute();
			
		} finally {

			ProcessContext.clear();
			
			ProcessContext.initialize(origCtx.getUserId(), origCtx.getUserDisplayName(), 
					origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
		}
		
		return result;
	}
	
	public static <T> T runAsSystem(Task<T> task) {
		
		ProcessContext origCtx = ProcessContext.get();
		
		T result = null;
		
		try {
			
			ProcessContext.clear();
			
			ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, AppConstants.SYSTEM_USER_NAME, 
					origCtx != null ? origCtx.getTenantId() : null, 
					origCtx != null ? origCtx.getTemplateId() : null, 
					origCtx != null ? origCtx.getClientId() : null);
			
			result = task.execute();
			
		} finally {

			ProcessContext.clear();
			
			if (origCtx != null) {
				ProcessContext.initialize(origCtx.getUserId(), origCtx.getUserDisplayName(), 
						origCtx.getTenantId(), origCtx.getTemplateId(), origCtx.getClientId());
			}
		}
		
		return result;
	}
	
	public interface Task<R> {
		R execute();
	}
	
}
