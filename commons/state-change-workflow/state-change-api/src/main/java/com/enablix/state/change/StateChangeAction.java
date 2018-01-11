package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public interface StateChangeAction<T extends RefObject, D extends ActionInput, V, R extends ActionResult<T, V>> {

	String getActionName();
	
	R execute(D actionData, T objectRef, StateChangeRecording<T> recording) throws ActionException;
	
	Class<D> getInputType();
	
}
