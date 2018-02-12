package com.enablix.bayes.finding.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.bayes.EBXNet;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.audit.repo.ActivityAuditRepository;

@Component
public class PeerAccessedContentTypeFinding extends AbstractPeerFinding implements NodeFindingProvider {

	@Autowired
	private ActivityAuditRepository auditRepo;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Value("${bayes.peers.content.type.access.finding.lookback:2}")
	private int lookback;
	
	@Override
	public String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec) {
		
		String finding = null;
		
		List<String> peersUserIds = getPeersUserIds(ctx, user);
		if (!CollectionUtil.isEmpty(peersUserIds)) {
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			List<ContainerType> linkedFromContainers = template.getLinkedFromContainers(dataRec.getContainerQId());
			
			List<String> qIds = linkedFromContainers == null ? new ArrayList<>() 
					: linkedFromContainers.stream().map(ContainerType::getQualifiedId).collect(Collectors.toList());
			
			qIds.add(dataRec.getContainerQId());
			
			Long count = auditRepo.countContentTypeAccessByUsersBetweenDates(
					qIds, peersUserIds, 
					ctx.getAdjustedRunAsDate(lookback), ctx.getRunAsDate());
			
			finding = count != null && count > 0 ? EBXNet.States.TRUE : EBXNet.States.FALSE;
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
