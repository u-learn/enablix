package com.enablix.bayes.content;

import com.enablix.bayes.EBXNet;
import com.enablix.bayes.EBXNet.EBXBoolNode;
import com.enablix.bayes.EBXNet.EBXHighLowNode;

public class ContentBayesNet {

	public static final String PEER_ACCESSED_CONTENT_TYPE_NT = "Peer Accessed Content Type";
	public static final String PEER_ACCESSED_CONTENT_TYPE_NN = "PEER_ACCESSED_CONTENT_TYPE";
	
	public static final String PEER_ACCESSED_CONTENT_NT = "Peer User Accessed Content";
	public static final String PEER_ACCESSED_CONTENT_NN = "PEER_ACCESSED_CONTENT";
	
	public static final String IS_POPULAR_AMONG_PEERS_NT = "Is content popular among peers";
	public static final String IS_POPULAR_AMONG_PEERS_NN = "IS_POPULAR_AMONG_PEERS";
	
	public static final String IS_CONTENT_UPDT_AFTER_ACCESS_NT = "Content Updated after Recent Access";
	public static final String IS_CONTENT_UPDT_AFTER_ACCESS_NN = "IS_CONTENT_UPDT_AFTER_ACCESS";
	
	public static final String IS_RECENTLY_UPDT_CONTENT_NT = "Content Updated";
	public static final String IS_RECENTLY_UPDT_CONTENT_NN = "IS_RECENTLY_UPDT_CONTENT";
	
	public static final String IS_NEW_CONTENT_NT = "Content Added";
	public static final String IS_NEW_CONTENT_NN = "IS_NEW_CONTENT";
	
	public static final String IS_RECENT_CONTENT_NT = "Is recent content";
	public static final String IS_RECENT_CONTENT_NN = "IS_RECENT_CONTENT";
	
	public static final String IS_RELEVANT_CONTENT_NT = "Is content interesting";
	public static final String IS_RELEVANT_CONTENT_NN = "IS_RELEVANT_CONTENT";

	public static EBXNet build() {
		
		EBXNet net = new EBXNet();
		BayesNetCPTables bayesCPTables = BayesNetCPTables.getInstance();
		
		// Target Node
		EBXBoolNode isRelevantContent = net.createBoolNode(IS_RELEVANT_CONTENT_NN, IS_RELEVANT_CONTENT_NT);
		net.target(isRelevantContent);
		
		/**
		 * ****************** Recent content sub-net: START ****************** 
		 */
		// Recent Content derived Node
		EBXBoolNode isRecentContent = net.createBoolNode(IS_RECENT_CONTENT_NN, IS_RECENT_CONTENT_NT);
		
		// New Content 
		EBXBoolNode isNewContent = net.createBoolNode(IS_NEW_CONTENT_NN, IS_NEW_CONTENT_NT);
		isNewContent.setCPTable(bayesCPTables.getNewContentCPT());
		isRecentContent.linkFrom(isNewContent);
		
		// Recent Updated content
		EBXBoolNode isRecentlyUpdated = net.createBoolNode(IS_RECENTLY_UPDT_CONTENT_NN, IS_RECENTLY_UPDT_CONTENT_NT);
		isRecentlyUpdated.setCPTable(bayesCPTables.getRecentlyUpdatedContentCPT());
		isRecentContent.linkFrom(isRecentlyUpdated);
		
		// Content updated after recent access
		EBXBoolNode isUpdatedAfterAccess = net.createBoolNode(IS_CONTENT_UPDT_AFTER_ACCESS_NN, IS_CONTENT_UPDT_AFTER_ACCESS_NT);
		isUpdatedAfterAccess.setCPTable(bayesCPTables.getContentUpdatedAfterAccessCPT());
		isRecentContent.linkFrom(isUpdatedAfterAccess);
		
		isRecentContent.setCPTable(bayesCPTables.getRecentContentCPT());
		isRelevantContent.linkFrom(isRecentContent);
		
		/** 
		 * ***************** Recent content sub-net: END ****************** 
		 **/
		
		//---------------------------------------------------------------------------------------------------//
		
		/**
		 * ****************** Popular Among Peers sub-net: START ****************** 
		 */
		// Recent Content derived Node
		EBXBoolNode isPopularAmongPeers = net.createBoolNode(IS_POPULAR_AMONG_PEERS_NN, IS_POPULAR_AMONG_PEERS_NT);
		
		// New Content 
		EBXHighLowNode accessedByPeer = net.createHighLowNode(PEER_ACCESSED_CONTENT_NN, PEER_ACCESSED_CONTENT_NT);
		accessedByPeer.setCPTable(bayesCPTables.getPeerAccessedContentCPT());
		isPopularAmongPeers.linkFrom(accessedByPeer);
		
		// Recent Updated content
		EBXHighLowNode peerAccessContentType = net.createHighLowNode(PEER_ACCESSED_CONTENT_TYPE_NN, PEER_ACCESSED_CONTENT_TYPE_NT);
		peerAccessContentType.setCPTable(bayesCPTables.getPeerAccessedContentTypeCPT());
		isPopularAmongPeers.linkFrom(peerAccessContentType);
		
		// Set CP Table for popular among peers content
		isPopularAmongPeers.setCPTable(bayesCPTables.getPopularAmongPeersCPT());
		isRelevantContent.linkFrom(isPopularAmongPeers);
		
		/** 
		 * ***************** Popular Among Peers sub-net: END ****************** 
		 **/
		
		isRelevantContent.setCPTable(bayesCPTables.getRelevantContentCPT());
		
		return net;
		
	}
	
}
