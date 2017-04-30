package com.enablix.analytics.info.detection;

import com.enablix.core.api.ContentRecord;

public class ContentSuggestion extends Opinion {

	private ContentRecord content;
	
	protected ContentSuggestion() {
		// for ORM
	}
	
	public ContentSuggestion(ContentRecord content, String opinionBy, float confidence) {
		super(opinionBy, confidence);
		this.content = content;
	}

	public ContentRecord getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "ContentSuggestion [content=" + content + "]";
	}

}
