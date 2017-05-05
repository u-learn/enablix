package com.enablix.analytics.info.detection.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.Assessment;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.LinkOpinion;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TaggedInfo;
import com.enablix.analytics.info.detection.TaggedInfoAnalyser;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.analytics.search.es.DefaultSearchFieldBuilder;
import com.enablix.analytics.search.es.ESQueryBuilder;
import com.enablix.analytics.search.es.ElasticSearchClient;
import com.enablix.analytics.search.es.SearchHitTransformer;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.DatastoreLocationType;
import com.enablix.services.util.TemplateUtil;

@Component
public class ESBasedLinkAnalyser extends TaggedInfoAnalyser {

	@Autowired
	private ElasticSearchClient esClient;
	
	@Autowired
	private SearchHitTransformer searchHitTx;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DefaultSearchFieldBuilder searchFieldBuilder;
	
	@Override
	public String name() {
		return "Elasticsearch linked content analyser";
	}

	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L1;
	}

	@Override
	protected Collection<Opinion> analyseTaggedInfo(TaggedInfo info, InfoDetectionContext ctx) {
		
		int pageSize = 10; // consider top 20 matches only
		int pageNum = 0;
		
		Set<Opinion> opinions = new HashSet<>();
		
		List<InfoTag> tags = info.tags();
		
		if (CollectionUtil.isNotEmpty(tags)) {
			
			TemplateFacade templateFacade = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			LinkContentCollector collector = new LinkContentCollector();
			
			LookupTypesAndContainers searchInTypes = getLinkedESTypesToLookup(ctx.getAssessment(), templateFacade);
			LinkAnalyserSearchFieldFilter fieldFilter = new LinkAnalyserSearchFieldFilter(searchInTypes.containerQIds);
			
			for (InfoTag tag : tags) {
				
				SearchHits result = null;
				pageNum = 0;
				
				do {
				
					SearchRequest request = ESQueryBuilder.builder(tag.tag(), templateFacade, searchFieldBuilder)
															.withPagination(pageSize, pageNum)
															.withFuzziness((searchTerm) -> Fuzziness.AUTO)
															.withTypeFilter((searchType) -> searchInTypes.types.contains(searchType))
															.withFieldFilter(fieldFilter)
															.withOptimizer((mmQueryBuilder) -> mmQueryBuilder.minimumShouldMatch("100%"))
															.build();
					
					result = esClient.searchContent(request);

					for (SearchHit hit : result) {
						
						ContentDataRef linkedRecord = searchHitTx.toContentDataRef(hit, templateFacade);
						collector.addLinkedContent(linkedRecord, hit.getScore());
					}
					
					pageNum++;
					
				} while (result != null && result.getTotalHits() > (pageNum * pageSize));
				
			}
			
			opinions = collector.getOpinions();
			
		}
		
		return opinions;
	}
	
	private class LinkContentCollector {
		
		private Map<String, Float> linkedContentMaxScore = new HashMap<>();
		private List<ContentAndScore> contentAndScoreRecords = new ArrayList<>();

		public void addLinkedContent(ContentDataRef linkedRecord, float score) {
			
			contentAndScoreRecords.add(new ContentAndScore(linkedRecord, score));
			
			String containerQId = linkedRecord.getContainerQId();
			Float existMaxScore = linkedContentMaxScore.get(containerQId);
			
			linkedContentMaxScore.put(containerQId, 
					Math.max(existMaxScore == null ? 0 : existMaxScore, score));
		}

		public Set<Opinion> getOpinions() {
			
			Set<Opinion> opinions = new HashSet<>();
			
			for (ContentAndScore entry : contentAndScoreRecords) {
			
				opinions.add(new LinkOpinion(entry.record, name(), 
						calculateHitConfidence(entry.score, 
								linkedContentMaxScore.get(entry.record.getContainerQId()))));
			}
			
			return opinions;
		}
		
	}
	
	private class ContentAndScore {
	
		private ContentDataRef record;
		private float score;

		private ContentAndScore(ContentDataRef record, float score) {
			super();
			this.record = record;
			this.score = score;
		}

	}

	private LookupTypesAndContainers getLinkedESTypesToLookup(Assessment assessment, TemplateFacade templateFacade) {
		
		LookupTypesAndContainers lookups = new LookupTypesAndContainers();
		
		for (TypeOpinion typeOp : assessment.getTypeOpinions()) {
			
			ContainerType container = templateFacade.getContainerDefinition(typeOp.getContainerQId());
			
			if (container != null) {
			
				for (ContentItemType contentItem : container.getContentItem()) {
				
					BoundedListDatastoreType refListDS = TemplateUtil.checkAndGetBoundedRefListDatastore(contentItem);
					
					if (refListDS != null && refListDS.getLocation() == DatastoreLocationType.CONTENT) {
					
						String collectionName = templateFacade.getCollectionName(refListDS.getStoreId());
						if (StringUtil.hasText(collectionName)) {
							lookups.types.add(collectionName);
							lookups.containerQIds.add(refListDS.getStoreId());
						}
					}
				}
			}
		}
		
		return lookups;
	}
	
	private static class LookupTypesAndContainers {
		private Set<String> types = new HashSet<>();
		private Set<String> containerQIds = new HashSet<>();
	}

	private float calculateHitConfidence(float hitScore, float maxScore) {
		return maxScore == 0 ? 0 : (hitScore/maxScore) * analyserConfidence();
	}
	
	private float analyserConfidence() {
		return 1;
	}

}
