package com.enablix.app.content.ui.search;

import java.util.List;

import com.enablix.app.content.ui.NavigableContent;

public interface SearchService {

	public List<NavigableContent> search(String searchText);
	
}
