package com.enablix.doc.state.change;

import com.enablix.doc.state.change.model.DocInfo;
import com.enablix.doc.state.change.model.DocPublishing;
import com.enablix.state.change.definition.RecordingInstantiator;

public class DocPublishingInstantiator implements RecordingInstantiator<DocInfo, DocPublishing> {

	@Override
	public DocPublishing newInstance() {
		return new DocPublishing();
	}

}
