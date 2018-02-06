package com.enablix.bayes.finding.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.bayes.ExecutionContext;
import com.enablix.core.domain.security.authorization.UserProfile;

public class AbstractPeerFinding {

	@Autowired
	private PeerUsersResolver peerResolver;

	public AbstractPeerFinding() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected List<String> getPeersUserIds(ExecutionContext ctx, UserProfile user) {
		
		Object object = ctx.getSharedAttributes().get("user.peers");
		
		Map<String, List<String>> peerUsers = null;
		
		if (object == null) {
			
			peerUsers = new HashMap<String, List<String>>();
			ctx.getSharedAttributes().put("user.peers", peerUsers);
			
		} else {
			peerUsers = (Map<String, List<String>>) object;
		}
		
		List<String> peers = peerUsers.get(user.getEmail());
		if (peers == null) {
			peers = peerResolver.getPeerUserIds(user);
			peerUsers.put(user.getEmail(), peers);
		}
		
		return peers;
	}

}