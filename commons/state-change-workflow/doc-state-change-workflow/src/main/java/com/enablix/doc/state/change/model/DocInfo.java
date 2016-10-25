package com.enablix.doc.state.change.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.RefObject;

public class DocInfo extends RefObject implements ActionInput {

	private String title;
	
	private String notes;

	@DBRef
	private DocumentMetadata metadata;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public DocumentMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(DocumentMetadata metadata) {
		this.metadata = metadata;
		setIdentity(metadata.getIdentity());
	}
	
}
