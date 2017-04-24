package com.enablix.wordpress.integration;

import java.util.List;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.model.Term;
import com.enablix.analytics.info.detection.ContentSuggestion;

public interface WordpressService {

	Wordpress createClient(String baseUrl);

	Post getPost(Wordpress wp, Long postId) throws PostNotFoundException;

	List<Term> getPostTags(Wordpress wp, Post post);
	
	List<ContentSuggestion> getContentSuggestion(Wordpress wp, Long postId);
	
}
