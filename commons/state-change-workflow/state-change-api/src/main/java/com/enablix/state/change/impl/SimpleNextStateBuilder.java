package com.enablix.state.change.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.util.StringUtil;
import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ObjectState;
import com.enablix.state.change.model.RefObject;

public class SimpleNextStateBuilder<T extends RefObject, R, I extends ActionInput> implements NextStateBuilder<T, R, I> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNextStateBuilder.class);
	
	private ActionRegistry<T> actionRegistry;
	private Map<NextStateId, String> nextStateConfig;
	
	public SimpleNextStateBuilder(ActionRegistry<T> actionRegistry) {
		this.actionRegistry = actionRegistry;
		this.nextStateConfig = new HashMap<>();
	}
	
	@Override
	public ObjectState nextState(ObjectState currentState, String actionName, R actionReturnValue, I actionInput) {
		
		NextStateId id = new NextStateId(actionName, currentState.getStateName());
		String nextStateName = nextStateConfig.get(id);
		
		if (StringUtil.isEmpty(nextStateName)) {
			LOGGER.error("No next state defined for: {}", id);
			throw new IllegalStateException("No next state defined for: " + id);
		}
		
		List<ActionDefinition> nextActions = actionRegistry.getNextAllowedActions(nextStateName);
		if (nextActions == null) {
			nextActions = new ArrayList<>();
		}
		
		ObjectState nextState = new ObjectState(nextStateName);
		nextState.setNextActions(nextActions);
		
		return nextState;
	}
	
	public void addNextStateConfig(String fromState, String actionTaken, String nextState) {
		NextStateId nextStateId = new NextStateId(actionTaken, fromState);
		nextStateConfig.put(nextStateId, nextState);
	}

	private static final class NextStateId {
		
		private final String actionName;
		private final String fromState;
		
		public NextStateId(String actionName, String fromState) {
			super();
			this.actionName = actionName;
			this.fromState = fromState;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
			result = prime * result + ((fromState == null) ? 0 : fromState.hashCode());
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
			NextStateId other = (NextStateId) obj;
			if (actionName == null) {
				if (other.actionName != null)
					return false;
			} else if (!actionName.equals(other.actionName))
				return false;
			if (fromState == null) {
				if (other.fromState != null)
					return false;
			} else if (!fromState.equals(other.fromState))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "[actionName=" + actionName + ", fromState=" + fromState + "]";
		}
		
	}
	
}
