package com.enablix.app.content.ui.reco;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.reco.Recommendation;
import com.enablix.core.domain.reco.RecommendationScope;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;

@Component
public class RecommendationManagerImpl implements RecommendationManager {

	@Autowired
	private RecommendationRepository repo;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
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
	public Collection<RecommendationWrapper> getAllGenericRecommendations() {
		
		Collection<Recommendation> recos = repo.findByTemplateId(ProcessContext.get().getTemplateId());
		
		Collection<RecommendationWrapper> recoWrappers = new ArrayList<>();
		
		if (recos != null) {
			
			for (Recommendation reco : recos) {
			
				NavigableContent navReco = navContentBuilder.build(reco.getRecommendedData().getData(), labelResolver);
				
				if (navReco != null) {
				
					RecommendationWrapper wrapper = new RecommendationWrapper();
					wrapper.setRecommendation(reco);
					wrapper.setNavContent(navReco);
					
					recoWrappers.add(wrapper);
				}
			}
		}
		
		return recoWrappers;
	}

}
