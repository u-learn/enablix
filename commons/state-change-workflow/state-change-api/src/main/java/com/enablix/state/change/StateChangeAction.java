package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;

public interface StateChangeAction<T extends RefObject, D extends ActionInput, V, R extends ActionResult<T, V>> {

	String getActionName();
	
	R execute(D actionData, T objectRef) throws ActionException;
	
	Class<D> getInputType();
	
}
