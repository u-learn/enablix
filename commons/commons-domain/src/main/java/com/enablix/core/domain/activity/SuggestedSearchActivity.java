package com.enablix.core.domain.activity;

import java.util.Map;

public class SuggestedSearchActivity extends BaseSearchActivity {

	private String suggestionType;
	
	private Map<String, String> suggestion;
	
	private String typedText;
	
	protected SuggestedSearchActivity() {
		super(ActivityType.SUGGESTED_SEARCH, null);
	}
	
	public SuggestedSearchActivity(String searchTerm) {
		super(ActivityType.SUGGESTED_SEARCH, searchTerm);
	}

	public String getSuggestionType() {
		return suggestionType;
	}

	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
	}

	public Map<String, String> getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(Map<String, String> suggestion) {
		this.suggestion = suggestion;
	}

	public String getTypedText() {
		return typedText;
	}

	public void setTypedText(String typedText) {
		this.typedText = typedText;
	}
	
	
}
