package com.enablix.task;

public class TaskUtil {

	public static boolean isPerTenantTask(Task task) {
		PerTenantTask perTenantTaskAnnot = task.getClass().getAnnotation(PerTenantTask.class);
		return perTenantTaskAnnot != null;
	}
	
}
