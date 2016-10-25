package com.enablix.state.change.definition;

import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public interface RecordingInstantiator<T extends RefObject, A extends StateChangeRecording<T>> {

	A newInstance();
	
}
