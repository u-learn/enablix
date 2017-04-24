package com.enablix.wordpress.integration.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.model.Term;
import com.afrozaar.wordpress.wpapi.v2.util.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.util.ClientFactory;
import com.enablix.analytics.info.detection.ContentSuggestion;
import com.enablix.analytics.info.detection.InfoDetector;
import com.enablix.wordpress.integration.WordpressService;
import com.enablix.wordpress.model.WordpressInfo;

@Component
public class WordpressServiceImpl implements WordpressService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WordpressServiceImpl.class);
	
	@Autowired
	private InfoDetector infoDetector;
	
	@Override
	public Post getPost(Wordpress wp, Long postId) throws PostNotFoundException {
		Post post = wp.getPost(postId);
		return post;
	}

	@Override
	public List<Term> getPostTags(Wordpress wp, Post post) {
		return wp.getPostTags(post);
	}
	
	@Override
	public Wordpress createClient(String baseUrl) {
		return ClientFactory.fromConfig(ClientConfig.of(baseUrl, null, null, true));
	}

	@Override
	public List<ContentSuggestion> getContentSuggestion(Wordpress wp, Long postId) {
		
		List<ContentSuggestion> contentSuggestion = null;
		
		try {
			
			Post post = getPost(wp, postId);
			
			if (post != null) {
				List<Term> postTags = getPostTags(wp, post);
				WordpressInfo wpInfo = new WordpressInfo(post, postTags);
				contentSuggestion = infoDetector.analyse(wpInfo);
			}
			
		} catch (PostNotFoundException e) {
			LOGGER.error("Wordpress post [" + postId + "] not found", e);
		}
		
		return contentSuggestion;
	}
	
}
