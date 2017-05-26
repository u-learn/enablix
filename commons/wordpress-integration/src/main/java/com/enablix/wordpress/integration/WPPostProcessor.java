package com.enablix.wordpress.integration;

import com.afrozaar.wordpress.wpapi.v2.model.Post;

public interface WPPostProcessor {

	void process(Post post);
	
}
