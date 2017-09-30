package com.enablix.wordpress.integration.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.api.Contexts;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.model.Term;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.afrozaar.wordpress.wpapi.v2.response.PagedResponse;
import com.enablix.analytics.info.detection.ContentSuggestion;
import com.enablix.analytics.info.detection.InfoDetector;
import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.config.Configuration;
import com.enablix.wordpress.integration.WPConfigNotFoundException;
import com.enablix.wordpress.integration.WPPostProcessor;
import com.enablix.wordpress.integration.WordpressConstants;
import com.enablix.wordpress.integration.WordpressService;
import com.enablix.wordpress.model.WordpressInfo;

@Component
public class WordpressServiceImpl implements WordpressService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WordpressServiceImpl.class);
	
	private static final String POST_CATEGORIES = "/categories?post={postId}";
	
	@Autowired
	private InfoDetector infoDetector;
	
	@Override
	public Post getPost(Wordpress wp, Long postId) throws PostNotFoundException {
		Post post = wp.getPost(postId);
		return post;
	}

	@Override
	public Post getPostBySlug(Wordpress wp, String postSlug) {
		
		Post post = null;
		
		SearchRequest<Post> searchRequest = 
				SearchRequest.Builder.aSearchRequest(Post.class)
									 .withContext(Contexts.VIEW)
									 .withParam(WordpressConstants.WP_API_PARAM_SLUG, postSlug)
									 .build();

		PagedResponse<Post> posts = wp.search(searchRequest);
		
		if (posts != null) {
			
			List<Post> postList = posts.getList();
			if (CollectionUtil.isNotEmpty(postList)) {
				post = postList.get(0);
			}
		}
		
		return post;
	}
	
	@Override
	public List<Term> getPostTags(Wordpress wp, Post post) {
		return wp.getPostTags(post);
	}
	
	@Override
	public List<Term> getPostCategories(Wordpress wp, Post post) {
		
		List<Term> collected = new ArrayList<>();
        
		PagedResponse<Term> pagedResponse = wp.getPagedResponse(
				POST_CATEGORIES, Term.class, post.getId().toString());
		
		collected.addAll(pagedResponse.getList());
		
		while (pagedResponse.hasNext()) {
			pagedResponse = wp.traverse(pagedResponse, PagedResponse.NEXT);
			collected.addAll(pagedResponse.getList());
		}
		
		return collected;
	}

	@Override
	public List<Term> getPostTagsAndCategories(Wordpress wp, Post post) {
		
		List<Term> tags = wp.getPostTags(post);
		
		if (tags == null) {
			tags = new ArrayList<>();
		}
		
		tags.addAll(getPostCategories(wp, post));
		
		return tags;
	}
	
	@Override
	public Wordpress createClient(String baseUrl) {
		return ClientFactory.fromConfig(ClientConfig.of(baseUrl, null, null, true, true));
	}

	@Override
	public List<ContentSuggestion> getContentSuggestion(Wordpress wp, Long postId) throws PostNotFoundException {
		
		List<ContentSuggestion> contentSuggestion = null;
		
		try {
			
			Post post = getPost(wp, postId);
			
			contentSuggestion = buildContentSuggestion(wp, post);
			
		} catch (PostNotFoundException e) {
			LOGGER.error("Wordpress post [" + postId + "] not found", e);
			throw e;
		}
		
		return contentSuggestion;
	}

	private List<ContentSuggestion> buildContentSuggestion(Wordpress wp, Post post) {
		
		List<ContentSuggestion> contentSuggestion = null;
		
		if (post != null) {
			
			List<Term> postTags = getPostTagsAndCategories(wp, post);
			WordpressInfo wpInfo = new WordpressInfo(post, postTags);
			contentSuggestion = infoDetector.analyse(wpInfo);
		}
		
		return contentSuggestion;
	}

	@Override
	public List<ContentSuggestion> getContentSuggestion(Wordpress wp, String postSlug) throws PostNotFoundException {
		
		Post post = getPostBySlug(wp, postSlug);
		
		if (post == null) {
			LOGGER.error("Wordpress post [" + postSlug + "] not found");
			throw new PostNotFoundException(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		}
		
		return buildContentSuggestion(wp, post);
	}

	@Override
	public Wordpress createClient() throws WPConfigNotFoundException {
		
		Configuration wpConfig = ConfigurationUtil.getConfig(WordpressConstants.INTEGRATION_WORDPRESS_CONFIG_KEY);
		if (wpConfig == null) {
			throw new WPConfigNotFoundException("Wordpress configuration not found");
		}
		
		String baseUrl = wpConfig.getStringValue("BASE_URL");
		if (StringUtil.isEmpty(baseUrl)) {
			throw new WPConfigNotFoundException("Base URL not found in wordpress configuration");
		}

		return createClient(baseUrl);
	}

	@Override
	public void processPosts(Wordpress wp, WPPostProcessor postProcessor, SearchRequest<Post> postSearch) {
		
		PagedResponse<Post> response = null;

		do {
			
			if (response == null) {
				response = wp.search(postSearch);
			} else {
				response = wp.traverse(response, PagedResponse.NEXT);
			}
			
			for (Post post : response.getList()) {
				postProcessor.process(post);
			}
			
		} while (response.hasNext());
		
	}
	
}
