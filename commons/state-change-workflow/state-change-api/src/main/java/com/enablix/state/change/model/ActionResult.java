package com.enablix.state.change.model;

public interface ActionResult<T extends RefObject, V> {

	T updatedRefObject();
	
	V returnValue();
	
}
