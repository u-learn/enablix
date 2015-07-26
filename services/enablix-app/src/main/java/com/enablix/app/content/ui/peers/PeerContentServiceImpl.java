package com.enablix.app.content.ui.peers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.context.RequestContext;
import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.analytics.web.request.WebRequestContextBuilder;
import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;

@Component
public class PeerContentServiceImpl implements PeerContentService {

	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private WebRequestContextBuilder<WebContentRequest> requestCtxBuilder = 
			new WebRequestContextBuilder<WebContentRequest>();
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public List<NavigableContent> getPeers(WebContentRequest request) {
		
		RequestContext reqCtx = requestCtxBuilder.build(request);
		
		FetchContentRequest fetchRequest = new FetchContentRequest(reqCtx.templateId(), 
				reqCtx.containerQId(), null, reqCtx.contentIdentity());
		
		List<Map<String, Object>> peers = dataMgr.fetchPeers(fetchRequest);
		
		List<NavigableContent> navContent = new ArrayList<>();
		
		if (peers != null) {
			for (Map<String, Object> peer : peers) {
				navContent.add(navContentBuilder.build(
						peer, reqCtx.templateId(), reqCtx.containerQId(), labelResolver));
			}
		}
		
		return navContent;
	}

}
