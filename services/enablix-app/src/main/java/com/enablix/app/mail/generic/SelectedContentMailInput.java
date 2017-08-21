package com.enablix.app.mail.generic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.core.ui.DisplayableContent;

public class SelectedContentMailInput extends BasicEmailVelocityInput {

	private Map<String, List<DisplayableContent>> content;
	
	public SelectedContentMailInput() {
		this.content = new HashMap<>();
	}

	public Map<String, List<DisplayableContent>> getContent() {
		return content;
	}

	public void setContent(Map<String, List<DisplayableContent>> content) {
		this.content = content;
	}

	public void addContent(String contentQId, List<DisplayableContent> dcList) {
		content.put(contentQId, dcList);
	} 

}
