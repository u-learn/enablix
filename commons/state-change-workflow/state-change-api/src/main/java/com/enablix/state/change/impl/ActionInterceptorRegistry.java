package com.enablix.state.change.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.state.change.ActionInterceptor;

@SuppressWarnings("rawtypes")
@Component
public class ActionInterceptorRegistry extends SpringBackedBeanRegistry<ActionInterceptor> {

	private Map<String, WorkflowInterceptorRegistry> interceptorRegistry;
	
	public ActionInterceptorRegistry() {
		interceptorRegistry = new HashMap<String, WorkflowInterceptorRegistry>();
	}
	
	
	@Override
	protected Class<ActionInterceptor> lookupForType() {
		return ActionInterceptor.class;
	}
	
	public List<ActionInterceptor> getInterceptors(String workflowName, String actionName) {
		
		List<ActionInterceptor> interceptors = null;
		
		WorkflowInterceptorRegistry wfInterceptorRegistry = interceptorRegistry.get(workflowName);
		if (wfInterceptorRegistry != null) {
			interceptors = wfInterceptorRegistry.actionInterceptors.get(actionName);
		}
		
		return interceptors == null ? new ArrayList<ActionInterceptor>() : interceptors;
	}

	@Override
	protected void registerBean(ActionInterceptor bean) {
		
		WorkflowInterceptorRegistry wfInterceptorRegistry = interceptorRegistry.get(bean.workflowName());
		
		if (wfInterceptorRegistry == null) {
			wfInterceptorRegistry = new WorkflowInterceptorRegistry(bean.workflowName());
			interceptorRegistry.put(bean.workflowName(), wfInterceptorRegistry);
		}
		
		wfInterceptorRegistry.add(bean);
	}
	
	private static class WorkflowInterceptorRegistry {
		
		private String workflowName;
		private Map<String, List<ActionInterceptor>> actionInterceptors;
		
		private WorkflowInterceptorRegistry(String workflowName) {
			this.workflowName = workflowName;
			this.actionInterceptors = new HashMap<>();
		}

		private void add(ActionInterceptor<?, ?> bean) {
			for (String actionInterestedIn : bean.actionsInterestedIn()) {
				add(actionInterestedIn, bean);
			}
		}
		
		private void add(String actionName, ActionInterceptor bean) {
			
			List<ActionInterceptor> interceptors = actionInterceptors.get(actionName);
			
			if (interceptors == null) {
				interceptors = new ArrayList<>();
				actionInterceptors.put(actionName, interceptors);
			}
			
			interceptors.add(bean);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((workflowName == null) ? 0 : workflowName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WorkflowInterceptorRegistry other = (WorkflowInterceptorRegistry) obj;
			if (workflowName == null) {
				if (other.workflowName != null)
					return false;
			} else if (!workflowName.equals(other.workflowName))
				return false;
			return true;
		}
		
	}

}
