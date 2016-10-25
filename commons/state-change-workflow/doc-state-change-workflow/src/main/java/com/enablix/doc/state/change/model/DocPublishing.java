package com.enablix.doc.state.change.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.state.change.model.StateChangeRecording;

@Document(collection = "ebx_doc_publishing")
public class DocPublishing extends StateChangeRecording<DocInfo> {

}
