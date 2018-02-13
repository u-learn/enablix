package com.enablix.bayes.finding.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.bayes.EBXNet;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.mongodb.DBCollection;

public abstract class AbstractPeerFinding implements NodeFindingProvider {

	@Autowired
	private PeerUsersResolver peerResolver;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	

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
	
	@Override
	public String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec) {
		
		String finding = null;
		
		List<String> peersUserIds = getPeersUserIds(ctx, user);
		
		if (!CollectionUtil.isEmpty(peersUserIds)) {
			
			Query query = queryCriteria(ctx, dataRec, peersUserIds);
			
			DBCollection collection = mongoTemplate.getCollection("ebx_activity_audit");
			List<?> distinctUsers = collection.distinct("actor.userId", query.getQueryObject());
			
			if (CollectionUtil.isNotEmpty(distinctUsers)) {
				
				float accessPercent = ((float) distinctUsers.size() * 100) / peersUserIds.size();
				finding = accessPercent > getHighCutoffPercent() ? EBXNet.States.HIGH : EBXNet.States.LOW;
				
			} else {
				finding = EBXNet.States.NO;
			}
			
		}
		
		return finding;
	}

	protected abstract float getHighCutoffPercent();

	protected abstract Query queryCriteria(ExecutionContext ctx, ContentDataRecord dataRec, List<String> peersUserIds);

}