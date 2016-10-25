package com.enablix.state.change.model;

public class GenericActionResult<T extends RefObject, V> implements ActionResult<T, V> {

	private T refObject;
	private V returnValue;

	public GenericActionResult(T refObject, V returnValue) {
		super();
		this.refObject = refObject;
		this.returnValue = returnValue;
	}

	public T getRefObject() {
		return refObject;
	}

	public void setRefObject(T refObject) {
		this.refObject = refObject;
	}

	public V getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(V returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public T updatedRefObject() {
		return refObject;
	}

	@Override
	public V returnValue() {
		return returnValue;
	}

}
