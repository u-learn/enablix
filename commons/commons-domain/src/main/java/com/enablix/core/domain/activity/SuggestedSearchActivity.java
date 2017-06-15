package com.enablix.core.domain.activity;

import java.util.Map;

public class SuggestedSearchActivity extends ContextAwareActivity {

	private String suggestionType;
	
	private Map<String, String> suggestion;
	
	private String searchTerm;
	
	private String typedText;
	
	public SuggestedSearchActivity() {
		super(Category.SEARCH);
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

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getTypedText() {
		return typedText;
	}

	public void setTypedText(String typedText) {
		this.typedText = typedText;
	}
	
	
}
