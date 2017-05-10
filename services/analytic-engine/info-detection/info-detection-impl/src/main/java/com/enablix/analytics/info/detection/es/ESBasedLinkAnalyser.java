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

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoDetectorHelper;
import com.enablix.analytics.info.detection.InfoDetectorHelper.LookupCollectionAndContainers;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.LinkOpinion;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TaggedInfo;
import com.enablix.analytics.info.detection.TaggedInfoAnalyser;
import com.enablix.analytics.search.es.DefaultSearchFieldBuilder;
import com.enablix.analytics.search.es.ESQueryBuilder;
import com.enablix.analytics.search.es.ElasticSearchClient;
import com.enablix.analytics.search.es.SearchHitTransformer;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;

//@Component
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
		
		int pageSize = 10;
		int pageNum = 0;
		
		Set<Opinion> opinions = new HashSet<>();
		
		List<InfoTag> tags = info.tags();
		
		if (CollectionUtil.isNotEmpty(tags)) {
			
			TemplateFacade templateFacade = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			LinkContentCollector collector = new LinkContentCollector();
			
			LookupCollectionAndContainers searchInTypes = InfoDetectorHelper.getLinkedContentCollections(ctx.getAssessment(), templateFacade);
			
			HashSet<String> searchTypes = CollectionUtil.transform(searchInTypes.getEntries(), 
											() -> new HashSet<String>(), (entry) -> entry.getCollection());
			
			LinkAnalyserSearchFieldFilter fieldFilter = new LinkAnalyserSearchFieldFilter(searchTypes);
			
			for (InfoTag tag : tags) {
				
				SearchHits result = null;
				pageNum = 0;
				
				do {
				
					SearchRequest request = ESQueryBuilder.builder(tag.tag(), templateFacade, searchFieldBuilder)
															.withPagination(pageSize, pageNum)
															.withFuzziness((searchTerm) -> Fuzziness.AUTO)
															.withTypeFilter((searchType) -> searchTypes.contains(searchType))
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

	private float calculateHitConfidence(float hitScore, float maxScore) {
		return maxScore == 0 ? 0 : (hitScore/maxScore) * analyserConfidence();
	}
	
	private float analyserConfidence() {
		return 0.8f;
	}

}
