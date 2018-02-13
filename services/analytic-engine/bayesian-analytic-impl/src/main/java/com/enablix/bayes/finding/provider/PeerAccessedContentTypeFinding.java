package com.enablix.bayes.finding.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.services.util.ContentDataUtil;

@Component
public class PeerAccessedContentTypeFinding extends AbstractPeerFinding implements NodeFindingProvider {

	@Autowired
	private TemplateManager templateMgr;
	
	@Value("${bayes.peers.content.type.access.finding.lookback:2}")
	private int lookback;
	
	@Value("${bayes.peers.content.type.access.finding.high.cutoff.percent:40}")
	private float highCutoffPercent;
	
	protected float getHighCutoffPercent() {
		return highCutoffPercent;
	}

	protected Query queryCriteria(ExecutionContext ctx, ContentDataRecord dataRec, List<String> peersUserIds) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		List<ContainerType> linkedFromContainers = template.getLinkedFromContainers(dataRec.getContainerQId());
		
		List<String> qIds = linkedFromContainers == null ? new ArrayList<>() 
				: linkedFromContainers.stream().map(ContainerType::getQualifiedId).collect(Collectors.toList());
		
		qIds.add(dataRec.getContainerQId());
		
		String recIdentity = ContentDataUtil.getRecordIdentity(dataRec.getRecord());
		
		Criteria criteria = Criteria.where("activity.activityType").is("CONTENT_ACCESS")
				.and("activity.itemIdentity").ne(recIdentity)
				.and("activity.containerQId").in(qIds)
				.and("actor.userId").in(peersUserIds)
				.and("activityTime").gte(ctx.getAdjustedRunAsDate(-lookback)).lt(ctx.getRunAsDate());
		
		Query query = Query.query(criteria);
		return query;
	}
	
	@Override
	public String nodeName() {
		return ContentBayesNet.PEER_ACCESSED_CONTENT_TYPE_NN;
	}

	@Override
	public void init(InitializationContext ctx) {

		
	}

}
