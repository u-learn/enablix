package com.enablix.bayes.content;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bayes.net.cptable")
public class BayesNetCPTables {

	private float[] newContentCPT = {};
	
	private float[] recentContentCPT = {};
	
	private float[] recentlyUpdatedContentCPT = {};
	
	private float[] contentUpdatedAfterAccessCPT = {};
	
	private float[] popularAmongPeersCPT = {};
	
	private float[] peerAccessedContentCPT = {};
	
	private float[] peerAccessedContentTypeCPT = {};
	
	private float[] relevantContentCPT = {};
	
	private static BayesNetCPTables theInstance = null;
	
	public BayesNetCPTables() {
		theInstance = this;
	}

	public float[] getNewContentCPT() {
		return newContentCPT;
	}

	public void setNewContentCPT(float[] iS_NEW_CONTENT) {
		newContentCPT = iS_NEW_CONTENT;
	}

	public float[] getRecentContentCPT() {
		return recentContentCPT;
	}

	public void setRecentContentCPT(float[] iS_RECENT_CONTENT) {
		recentContentCPT = iS_RECENT_CONTENT;
	}

	public float[] getRecentlyUpdatedContentCPT() {
		return recentlyUpdatedContentCPT;
	}

	public void setRecentlyUpdatedContentCPT(float[] iS_RECENTLY_UPDT_CONTENT) {
		recentlyUpdatedContentCPT = iS_RECENTLY_UPDT_CONTENT;
	}

	public float[] getContentUpdatedAfterAccessCPT() {
		return contentUpdatedAfterAccessCPT;
	}

	public void setContentUpdatedAfterAccessCPT(float[] iS_CONTENT_UPDT_AFTER_ACCESS) {
		contentUpdatedAfterAccessCPT = iS_CONTENT_UPDT_AFTER_ACCESS;
	}

	public float[] getPopularAmongPeersCPT() {
		return popularAmongPeersCPT;
	}

	public void setPopularAmongPeersCPT(float[] iS_POPULAR_AMONG_PEERS) {
		popularAmongPeersCPT = iS_POPULAR_AMONG_PEERS;
	}

	public float[] getPeerAccessedContentCPT() {
		return peerAccessedContentCPT;
	}

	public void setPeerAccessedContentCPT(float[] pEER_ACCESSED_CONTENT) {
		peerAccessedContentCPT = pEER_ACCESSED_CONTENT;
	}

	public float[] getPeerAccessedContentTypeCPT() {
		return peerAccessedContentTypeCPT;
	}

	public void setPeerAccessedContentTypeCPT(float[] pEER_ACCESSED_CONTENT_TYPE) {
		peerAccessedContentTypeCPT = pEER_ACCESSED_CONTENT_TYPE;
	}

	public float[] getRelevantContentCPT() {
		return relevantContentCPT;
	}

	public void setRelevantContentCPT(float[] iS_RELEVANT_CONTENT) {
		relevantContentCPT = iS_RELEVANT_CONTENT;
	}
	
	public static BayesNetCPTables getInstance() {
		return theInstance;
	}
	
}
