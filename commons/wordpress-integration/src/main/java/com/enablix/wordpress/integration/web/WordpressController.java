package com.enablix.wordpress.integration.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.enablix.analytics.info.detection.ContentSuggestion;
import com.enablix.wordpress.integration.WordpressService;

@RestController
@RequestMapping("wp")
public class WordpressController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WordpressController.class);
	
	@Autowired
	private WordpressService wpClient;
	
	@RequestMapping(method = RequestMethod.POST, value="/post/{id}/", produces = "application/json")
	public Post fetchWPPost(@RequestBody WPSite wp, @PathVariable Long id) 
			throws NumberFormatException, PostNotFoundException {
		
		LOGGER.debug("Getting Wordpress post: {}", id);
		
		Wordpress client = wpClient.createClient(wp.baseUrl);
		return wpClient.getPost(client, id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cs/post/{id}/", produces = "application/json")
	public List<ContentSuggestion> getWPPostContentSuggestions(
			@RequestBody WPSite wp, @PathVariable Long id) throws PostNotFoundException {
		
		LOGGER.debug("Getting Content suggestion for Wordpress post: {}", id);
		
		Wordpress client = wpClient.createClient(wp.baseUrl);
		List<ContentSuggestion> contentSuggestion = wpClient.getContentSuggestion(client, id);
		LOGGER.debug("content suggestion: " + contentSuggestion);
		return contentSuggestion;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cs/postslug/{postSlug}/", produces = "application/json")
	public List<ContentSuggestion> getWPPostContentSuggestions(
			@RequestBody WPSite wp, @PathVariable String postSlug) throws PostNotFoundException {
		
		LOGGER.debug("Getting Content suggestion for Wordpress post: {}", postSlug);
		
		Wordpress client = wpClient.createClient(wp.baseUrl);
		return wpClient.getContentSuggestion(client, postSlug);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/cs/postslug/{postSlug}/", produces = "application/json")
	public List<ContentSuggestion> getWPPostslugContentSuggestions(@PathVariable String postSlug) throws Exception {
		
		LOGGER.debug("Getting Content suggestion for Wordpress post: {}", postSlug);
		
		Wordpress client = wpClient.createClient();
		return wpClient.getContentSuggestion(client, postSlug);
	}
	
	public static class WPSite {
		
		private String baseUrl;

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}
		
	}
	
}
