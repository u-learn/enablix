package com.enablix.app.content.ui.peers;

import java.util.List;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.data.view.DataView;

public interface PeerContentService {

	List<NavigableContent> getPeers(WebContentRequest request, DataView view);
	
}
