package com.enablix.app.content.coverage;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentTypeCoverageDTO;
import com.enablix.core.api.SearchRequest.Pagination;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.content.summary.ContentCoverage;

@Component
public class ContentCoverageServiceImpl implements ContentCoverageService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public Page<ContentTypeCoverageDTO> getContentTypeCoverage(Pagination pagination) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		Collection<String> businessContentQIds = template.getBizContentContainers().stream()
			.map(ContainerType::getQualifiedId).collect(Collectors.toList());
		
		Criteria criteria = Criteria.where("latest").is(Boolean.TRUE).and("contentQId").in(businessContentQIds);
		
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("contentQId")
						   .first("asOfDate").as("asOfDate")
						   .max("recordCreatedAt").as("lastCreateDt")
						   .max("recordModifiedAt").as("lastUpdateDt")
						   .count().as("count")
			);
			
		AggregationResults<ContentTypeCoverageDTO> result = 
				mongoTemplate.aggregate(agg, ContentCoverage.class, ContentTypeCoverageDTO.class);
			
		List<ContentTypeCoverageDTO> mappedResults = result.getMappedResults();
		
		return createResultPage(mappedResults, template, pagination);
	}
	
	private Page<ContentTypeCoverageDTO> createResultPage(List<ContentTypeCoverageDTO> mappedResults,
			TemplateFacade template, Pagination pagination) {
		
		// sorting and paginating on java service layers as there may be content types without an
		// record in content coverage and hence would not show up in the result
		
		Date asOfDate = CollectionUtil.isNotEmpty(mappedResults) ? mappedResults.get(0).getAsOfDate() : new Date();
		
		// Insert the missing records
		Map<String, ContentTypeCoverageDTO> coverageMap = 
				mappedResults.stream().collect(Collectors.toMap((e) -> e.getId(), (e) -> e));
		
		for (ContainerType container : template.getBizContentContainers()) {
			
			ContentTypeCoverageDTO entry = coverageMap.get(container.getQualifiedId());

			if (entry == null) {
				
				entry = new ContentTypeCoverageDTO();
				entry.setId(container.getQualifiedId());
				entry.setCount(0L);
				entry.setContainerLabel(container.getLabel());
				entry.setAsOfDate(asOfDate);
				coverageMap.put(container.getQualifiedId(), entry);
				
			} else {
				entry.setContainerLabel(container.getLabel());
			}
		}
	
		// Paginate the result
		Pageable pageable = pagination.toPageableObject();
		Comparator<ContentTypeCoverageDTO> comparator = null;
		boolean asc = pagination.getSort().getDirection() == Direction.ASC;
		
		switch (pagination.getSort().getField()) {
			case "containerLabel":
				comparator = asc ? (o1, o2) -> o1.getContainerLabel().compareTo(o2.getContainerLabel())
								 : (o1, o2) -> o2.getContainerLabel().compareTo(o1.getContainerLabel());
				break;
				
			case "lastUpdateDt": 
				comparator = new NullableDateComparator<>(asc, (o) -> o.getLastUpdateDt());
				break;
				
			case "lastCreateDt": 
				comparator = new NullableDateComparator<>(asc, (o) -> o.getLastCreateDt());
				break;
				
			case "count": 
				comparator = asc ? (o1, o2) -> o1.getCount().compareTo(o2.getCount())
				 		 		 : (o1, o2) -> o2.getCount().compareTo(o1.getCount());
				break;
				
			default:
				comparator = (o1, o2) -> o1.getContainerLabel().compareTo(o2.getContainerLabel());
				break;
		}
		
		
		List<ContentTypeCoverageDTO> all = coverageMap.values().stream().collect(Collectors.toList());
		Collections.sort(all, comparator);
		
		List<ContentTypeCoverageDTO> pageContent = CollectionUtil.getSubList(
				all, pageable.getOffset(), pageable.getOffset() + pageable.getPageSize());
		
		return new PageImpl<>(pageContent, pageable, all.size());
	}
	
	
	
	private static class NullableDateComparator<T> implements Comparator<T> {

		private boolean directionAsc;
		private Function<T, Date> dateProvider;
		
		private NullableDateComparator(boolean dirAsc, Function<T, Date> dateProvider) {
			this.directionAsc = dirAsc;
			this.dateProvider = dateProvider;
		}
		
		@Override
		public int compare(T o1, T o2) {
			
			Date d1 = dateProvider.apply(o1);
			Date d2 = dateProvider.apply(o2);
			
			if (d1 == d2) {
				// both may be null
				return 0;
			}
			
			if (d1 == null && d2 != null) {
				return 1;
			}
			
			if (d1 != null && d2 == null) {
				return -1;
			}
			
			return directionAsc ? d1.compareTo(d2) : d2.compareTo(d1);
		}
		
	}

}
