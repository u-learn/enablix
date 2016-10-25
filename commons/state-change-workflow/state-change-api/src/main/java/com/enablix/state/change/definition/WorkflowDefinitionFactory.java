package com.enablix.state.change.definition;

public interface WorkflowDefinitionFactory {

	StateChangeWorkflowDefinition<?, ?> getWorkflowDefinition(String workflowName);
	
}
