package com.enablix.state.change.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.state.change.definition.StateChangeWorkflowDefinition;
import com.enablix.state.change.definition.WorkflowDefinitionFactory;

@SuppressWarnings("rawtypes")
@Component
public class WorkflowDefinitionFactoryImpl extends SpringBackedBeanRegistry<StateChangeWorkflowDefinition> implements WorkflowDefinitionFactory {

	private Map<String, StateChangeWorkflowDefinition> registry = new HashMap<>();
	
	@Override
	public StateChangeWorkflowDefinition<?, ?> getWorkflowDefinition(String workflowName) {
		return registry.get(workflowName);
	}

	@Override
	protected Class<StateChangeWorkflowDefinition> lookupForType() {
		return StateChangeWorkflowDefinition.class;
	}

	@Override
	protected void registerBean(StateChangeWorkflowDefinition bean) {
		registry.put(bean.workflowName(), bean);
	}

}
