package com.enablix.app.content.ui.reco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.OrderAware;
import com.enablix.core.domain.reco.Recommendation;
import com.enablix.core.domain.reco.RecommendationScope;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.domain.tenant.TenantClient;
import com.enablix.core.security.auth.repo.ClientRepository;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;

@Component
public class RecommendationManagerImpl implements RecommendationManager {

	@Autowired
	private RecommendationRepository repo;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
	@Autowired
	private ClientRepository clientRepo;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public void saveRecommendation(Recommendation reco) {
		
		RecommendationScope scope = reco.getRecommendationScope();
		
		Collection<Recommendation> existingReco = 
				repo.findByRecommendationScopeAndRecommendationData(
							scope != null ? scope.getUserId() : null, 
							scope != null ? scope.getTemplateId() : null,
							scope != null ? scope.getContainerQId() : null,
							scope != null ? scope.getContentIdentity() : null,
							reco.getRecommendedData().getData().getContainerQId(), 
							reco.getRecommendedData().getData().getInstanceIdentity());

		if (existingReco == null || existingReco.isEmpty()) {
			
			DataSegmentInfo dsInfo = dsInfoBuilder.build(reco.getRecommendedData().getData());
			reco.setDataSegmentInfo(dsInfo);
			
			repo.save(reco);
		}
		
	}

	@Override
	public void deleteRecommendation(String recommendationIdentity) {
		repo.deleteByIdentity(recommendationIdentity);
	}

	@Override
	public Collection<RecommendationWrapper> getAllRecommendations() {
		
		Collection<Recommendation> recos = repo.findAll(OrderAware.SORT_BY_ORDER);
		
		List<TenantClient> clients = clientRepo.findAll();
		
		Map<String, TenantClient> clientIdMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(clients)) {
			for (TenantClient client : clients) {
				clientIdMap.put(client.getClientId(), client);
			}
		}
		
		Collection<RecommendationWrapper> recoWrappers = new ArrayList<>();
		
		if (recos != null) {
			
			for (Recommendation reco : recos) {
			
				NavigableContent navReco = navContentBuilder.build(reco.getRecommendedData().getData(), labelResolver);
				
				if (navReco != null) {
				
					RecommendationWrapper wrapper = new RecommendationWrapper();
					wrapper.setRecommendation(reco);
					wrapper.setNavContent(navReco);

					TenantClient client = clientIdMap.get(reco.getRecommendationScope().getClientId());
					if (client != null) {
						wrapper.setClientName(client.getClientName());
					}
					
					wrapper.setIdentity(reco.getIdentity());
					wrapper.setOrder(reco.getOrder());

					recoWrappers.add(wrapper);
				}
			}
		}
		
		return recoWrappers;
	}

}
