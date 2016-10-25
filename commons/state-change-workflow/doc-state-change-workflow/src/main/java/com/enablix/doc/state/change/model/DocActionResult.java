package com.enablix.doc.state.change.model;

import com.enablix.state.change.model.GenericActionResult;

public class DocActionResult<V> extends GenericActionResult<DocInfo, V> {

	public DocActionResult(DocInfo refObject, V returnValue) {
		super(refObject, returnValue);
	}

}
