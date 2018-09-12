package com.enablix.app.content.engagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.summary.repo.ContentCoverageRepository;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.ContentEngagementDTO;
import com.enablix.core.api.ContentEngagementRequest;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.activity.Activity;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.content.summary.ContentCoverage;
import com.enablix.core.mongo.aggregation.SortOperationWithoutCheck;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Component
public class ContentEngagementServiceImpl implements ContentEngagementService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ContentCoverageRepository contentCoverageRepo;
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public Page<ContentEngagementDTO> getContentEngagement(ContentEngagementRequest request) {

		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		Collection<String> businessContentQIds = request.getContentQIdIn();
		
		if (CollectionUtil.isEmpty(businessContentQIds)) {
			businessContentQIds = template.getBizContentContainers().stream()
					.map(ContainerType::getQualifiedId).collect(Collectors.toList());
		}
		
		List<String> activityTypes = new ArrayList<>();
		activityTypes.add(Activity.ActivityType.CONTENT_ACCESS.toString());
		activityTypes.add(Activity.ActivityType.DOC_DOWNLOAD.toString());
		
		Criteria criteria = Criteria.where("activity.containerQId").in(businessContentQIds)
				.and("activity.activityType").in(activityTypes)
				.and("activityTime").gte(request.getStartDate());
		
		Aggregation countAgg = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("$activity.itemIdentity"),
				Aggregation.group().count().as("total")
			);
		
		AggregationResults<AggCountResult> aggResult = mongoTemplate.aggregate(countAgg, ActivityAudit.class, AggCountResult.class);
		AggCountResult aggCountResult = aggResult.getUniqueMappedResult();
		
		List<ContentEngagementDTO> mappedResults = null;
		long count = aggCountResult != null ? aggCountResult.getTotal() : 0;
		
		Pageable pageable = request.getPagination().toPageableObject();
		
		if (count > 0) { 
			Aggregation agg = Aggregation.newAggregation(
					Aggregation.match(criteria),
					new CustomGroupOperation(),
					new SortOperationWithoutCheck(pageable.getSort()),
					Aggregation.skip(pageable.getOffset()),
					Aggregation.limit(pageable.getPageSize())
				);
			
			AggregationResults<ContentEngagementDTO> aggDtoResult = 
					mongoTemplate.aggregate(agg, ActivityAudit.class, ContentEngagementDTO.class);
			
			mappedResults = aggDtoResult.getMappedResults();
			
			populateContentDetails(mappedResults, template);
			
		} else {
			mappedResults = new ArrayList<>();
		}
		
		return new PageImpl<>(mappedResults, pageable, count);
	}
	
	private void populateContentDetails(List<ContentEngagementDTO> mappedResults, TemplateFacade template) {
		
		Collection<String> contentIdentities = mappedResults.stream().map((dto) -> dto.getId()).collect(Collectors.toList());
		List<ContentCoverage> contentCoverage = contentCoverageRepo.findByIdentityInAndLatestTrue(contentIdentities);
		
		Map<String, ContentCoverage> identityMap = new HashMap<>();
		contentCoverage.forEach((cc) -> identityMap.put(cc.getRecordIdentity(), cc));
		
		mappedResults.forEach((dto) -> {
			
			ContentCoverage cc = identityMap.get(dto.getId());
			
			if (cc != null) {
			
				dto.setContentTitle(cc.getRecordTitle());
				
			} else {
				
				ContentRecordRef dataRef = ContentDataRef.createContentRef(
						template.getId(), dto.getContainerQId(), dto.getId(), null);
				
				Map<String, Object> contentRecord = dataMgr.getContentRecord(dataRef, template, DataViewUtil.allDataView());
				if (contentRecord != null) {
					dto.setContentTitle(ContentDataUtil.getRecordTitle(contentRecord));
				}
			}
			
		});
	}

	private static class CustomGroupOperation implements AggregationOperation {

		private DBObject operation;
		
		private CustomGroupOperation() {
			
			this.operation = new BasicDBObject(
					"$group", new BasicDBObject(
						"_id", "$activity.itemIdentity"
					)
					.append("containerQId", new BasicDBObject("$first", "$activity.containerQId"))
					.append("contentTitle", new BasicDBObject("$first", "$activity.itemTitle"))
					.append("accessCount", new BasicDBObject(
								"$sum", new BasicDBObject("$cond", new Object[] {
											new BasicDBObject("$eq", new Object[] { "$activity.activityType", "CONTENT_ACCESS" }),
											1, 0
										})
								)
							)
					.append("downloadCount", new BasicDBObject(
							"$sum", new BasicDBObject("$cond", new Object[] {
										new BasicDBObject("$eq", new Object[] { "$activity.activityType", "DOC_DOWNLOAD" }),
										1, 0
									})
							)
						)
			); 
		}
		
		@Override
		public DBObject toDBObject(AggregationOperationContext context) {
			return context.getMappedObject(operation);
		}
		
	}
	
	private static class AggCountResult {
		
		private long total;

		public long getTotal() {
			return total;
		}

		@SuppressWarnings("unused")
		public void setTotal(long total) {
			this.total = total;
		}
		
		
	}

}
