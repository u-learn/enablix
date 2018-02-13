package com.enablix.bayes.finding.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.services.util.ContentDataUtil;

@Component
public class PeerAccessedContentFinding extends AbstractPeerFinding  implements NodeFindingProvider {

	@Value("${bayes.peers.content.access.finding.lookback:2}")
	private int lookback;
	
	@Value("${bayes.peers.content.access.finding.high.cutoff.percent:40}")
	private float highCutoffPercent;
	
	protected float getHighCutoffPercent() {
		return highCutoffPercent;
	}

	protected Query queryCriteria(ExecutionContext ctx, ContentDataRecord dataRec, List<String> peersUserIds) {
		
		String recIdentity = ContentDataUtil.getRecordIdentity(dataRec.getRecord());
		
		Criteria criteria = Criteria.where("activity.activityType").is("CONTENT_ACCESS")
				.and("activity.itemIdentity").is(recIdentity)
				.and("actor.userId").in(peersUserIds)
				.and("activityTime").gte(ctx.getAdjustedRunAsDate(-lookback)).lt(ctx.getRunAsDate());
		
		Query query = Query.query(criteria);
		return query;
	}

	@Override
	public String nodeName() {
		return ContentBayesNet.PEER_ACCESSED_CONTENT_NN;
	}

	@Override
	public void init(InitializationContext ctx) {

		
	}

}
