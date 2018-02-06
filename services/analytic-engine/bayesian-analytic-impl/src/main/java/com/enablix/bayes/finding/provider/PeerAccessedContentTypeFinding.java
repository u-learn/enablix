package com.enablix.bayes.finding.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.bayes.EBXNet;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.audit.repo.ActivityAuditRepository;

@Component
public class PeerAccessedContentTypeFinding extends AbstractPeerFinding implements NodeFindingProvider {

	@Autowired
	private ActivityAuditRepository auditRepo;
	
	@Value("${bayes.peers.content.type.access.finding.lookback:2}")
	private int lookback;
	
	@Override
	public String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec) {
		
		String finding = null;
		
		List<String> peersUserIds = getPeersUserIds(ctx, user);
		if (!CollectionUtil.isEmpty(peersUserIds)) {
			
			Long count = auditRepo.countContentTypeAccessByUsersBetweenDates(
					dataRec.getContainerQId(), peersUserIds, 
					ctx.getAdjustedRunAsDate(lookback), ctx.getRunAsDate());
			
			finding = count != null && count > 0 ? EBXNet.TRUE : EBXNet.FALSE;
		}
		
		return finding;
	}
	
	@Override
	public String nodeName() {
		return ContentBayesNet.PEER_ACCESSED_CONTENT_TYPE_NN;
	}

	@Override
	public void init(InitializationContext ctx) {

		
	}

}
