package com.enablix.state.change.impl;

import com.enablix.state.change.ActionInterceptor;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public abstract class ActionInterceptorAdapter<T extends RefObject, S extends StateChangeRecording<T>> implements ActionInterceptor<T, S> {

	@Override
	public void onActionStart(String actionName, ActionInput actionIn, S recording) {
		// empty function for easy extension
	}

	@Override
	public void onActionComplete(String actionName, ActionInput actionIn, S recording) {
		// empty function for easy extension
	}

	@Override
	public void onError(String actionName, ActionInput actionIn, S recording, Throwable e) {
		// empty function for easy extension
	}

}
