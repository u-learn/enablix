package com.enablix.wordpress.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.model.Term;
import com.enablix.analytics.info.detection.ContentAwareInfo;
import com.enablix.analytics.info.detection.IdentifiableInfo;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.LinkAwareInfo;
import com.enablix.analytics.info.detection.TaggedInfo;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.wordpress.integration.WordpressConstants;

public class WordpressInfo implements Information, TaggedInfo, IdentifiableInfo, LinkAwareInfo, ContentAwareInfo {

	private Post post;

	private List<Term> postTags;
	
	private List<InfoTag> infoTags; 
	
	protected WordpressInfo() {
		// for ORM
	}
	
	public WordpressInfo(Post post, List<Term> postTags) {
		
		super();
		
		this.post = post;
		this.postTags = postTags;
		
		infoTags = CollectionUtil.transform(postTags, 
					() -> new ArrayList<InfoTag>(), 
					(term) -> new WPInfoTag(term));
	}

	public Post getPost() {
		return post;
	}

	public List<Term> getPostTags() {
		return postTags;
	}

	public List<InfoTag> getInfoTags() {
		return infoTags;
	}

	@Override
	public String source() {
		return WordpressConstants.INFO_SRC_WP;
	}

	@Override
	public String type() {
		return WordpressConstants.INFO_TYPE_WP_POST;
	}

	@Override
	public List<InfoTag> tags() {
		return infoTags;
	}

	@Override
	public String identifier() {
		return String.valueOf(post.getId());
	}

	@Override
	public String content() {
		return post.getContent().getRaw();
	}

	@Override
	public String infoLink() {
		return post.getLink();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> infoData() {
		return (Map<String, Object>) BeanUtil.beanToMap(this);
	}

	@Override
	public void addTags(Collection<InfoTag> infoTags) {
		if (CollectionUtil.isNotEmpty(infoTags)) {
			this.infoTags.addAll(infoTags);
		}
	}
	
}
