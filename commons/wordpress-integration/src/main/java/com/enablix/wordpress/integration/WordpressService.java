package com.enablix.wordpress.integration;

import java.util.List;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.model.Term;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.enablix.analytics.info.detection.ContentSuggestion;

public interface WordpressService {

	Wordpress createClient(String baseUrl);
	
	Wordpress createClient() throws WPConfigNotFoundException; // lookup wordpress integration and create client based on that

	Post getPost(Wordpress wp, Long postId) throws PostNotFoundException;

	List<Term> getPostTags(Wordpress wp, Post post);
	
	List<ContentSuggestion> getContentSuggestion(Wordpress wp, Long postId) throws PostNotFoundException;
	
	List<ContentSuggestion> getContentSuggestion(Wordpress wp, String postSlug) throws PostNotFoundException;

	Post getPostBySlug(Wordpress wp, String postSlug);
	
	void processPosts(Wordpress wp, WPPostProcessor postProcessor, SearchRequest<Post> postSearch);

	List<Term> getPostCategories(Wordpress wp, Post post);

	List<Term> getPostTagsAndCategories(Wordpress wp, Post post);
	
}
