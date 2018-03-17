package com.enablix.app.content.ui.reco;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.builder.RecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.analytics.recommendation.impl.RelevantRecommendationEngine;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@Component
public class RecommendedContentServiceImpl implements RecommendedContentService {

	@Autowired
	private RecommendationEngine recoEngine;
	
	@Autowired
	private RecommendationContextBuilder<WebRecommendationRequest> recoCtxBuilder;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	@Autowired
	private RelevantRecommendationEngine relevanceEngine;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao dao;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public List<NavigableContent> getRecommendedContent(WebRecommendationRequest request, DataView dataView) {
		
		RecommendationContext recoCtx = recoCtxBuilder.build(request);
		
		List<ContentDataRef> recommendations = recoEngine.getRecommendations(recoCtx, dataView);
		
		List<NavigableContent> navRecos = new ArrayList<>();
		
		for (ContentDataRef reco : recommendations) {
			NavigableContent navReco = navContentBuilder.build(reco, labelResolver);
			if (navReco != null) {
				navRecos.add(navReco);
			}
		}
		
		return navRecos;
	}

	@Override
	public List<ContentDataRef> getAIRecommendedContent(WebRecommendationRequest request, DataView dataView) {
		
		RecommendationContext recoCtx = recoCtxBuilder.build(request);
		
		List<ContentDataRef> recommendations = relevanceEngine.getRecommendations(recoCtx, dataView);
		
		if (CollectionUtil.isEmpty(recommendations)) {
			// return the recent content
			List<ContentDataRef> recentContent = recentContent(dataView);
			return recentContent == null ? new ArrayList<>() : recentContent;
		}
		
		return recommendations;
	}
	
	private List<ContentDataRef> recentContent(DataView dataView) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		List<String> contentContQIds = template.getBizContentContainers().stream()
				.map(ContainerType::getQualifiedId).collect(Collectors.toList());
		
		Criteria criteria = Criteria.where("obsolete").is(false).and("data.containerQId").in(contentContQIds);
		Pageable pageable = new PageRequest(0, 5, new Sort(Direction.DESC, ContentDataConstants.CREATED_AT_KEY));
		
		Page<RecentData> page = dao.findByCriteria(criteria, RecentData.class, pageable, DataViewUtil.getMongoDataView(dataView));
		List<RecentData> records = page.getContent();
		
		if (CollectionUtil.isNotEmpty(records)) {
			return records.stream().map(RecentData::getData).collect(Collectors.toList());
		}
		
		return null;
	}

}
