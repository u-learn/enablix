package com.enablix.app.content.ui.peers;

import java.util.List;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;

public interface PeerContentService {

	List<NavigableContent> getPeers(WebContentRequest request);
	
}
