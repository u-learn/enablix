package com.enablix.content.approval.action;

import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.GenericActionResult;

public abstract class BaseContentAction<I extends ActionInput, V> implements StateChangeAction<ContentDetail, I, V, GenericActionResult<ContentDetail, V>> {

	private Class<I> inputType;
	
	public BaseContentAction(Class<I> inputType) {
		this.inputType = inputType;
	}

	public Class<I> getInputType() {
		return inputType;
	}
	
}
