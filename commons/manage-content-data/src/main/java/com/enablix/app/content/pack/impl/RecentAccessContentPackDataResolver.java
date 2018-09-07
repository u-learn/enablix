package com.enablix.app.content.pack.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.pack.ContentPackDataResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.activity.Activity;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.core.domain.content.pack.ContentPack.Type;
import com.enablix.data.view.DataView;

@Component
public class RecentAccessContentPackDataResolver implements ContentPackDataResolver<ContentPack>{

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Page<ContentDataRecord> getData(ContentPack pack, DataView dataView, int pageNo, int pageSize) {

		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		Collection<String> businessContentQIds = template.getBizContentContainers().stream()
			.map(ContainerType::getQualifiedId).collect(Collectors.toList());
		
		Criteria criteria = Criteria.where("activity.activityType").is(Activity.ActivityType.CONTENT_ACCESS)
				.and("actor.userId").is(ProcessContext.get().getUserId())
				.and("activity.containerQId").in(businessContentQIds);
		
		PageRequest pageable = new PageRequest(pageNo, pageSize, Direction.DESC, "activityTime");
		
		Aggregation countAgg = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.group("activity.itemIdentity"),
				Aggregation.group().count().as("total")
			);
		
		AggregationResults<AuditCountResult> countAggResult = mongoTemplate.aggregate(countAgg, ActivityAudit.class, AuditCountResult.class);
		AuditCountResult count = countAggResult.getUniqueMappedResult();
		
		Page<ContentDataRecord> dataPage = null;
		
		if (count.getTotal() > 0) {
			
			Aggregation agg = Aggregation.newAggregation(
						Aggregation.match(criteria),
						Aggregation.group("activity.itemIdentity")
								   .first("activity.containerQId").as("containerQId")
								   .max("activityTime").as("activityTime"),
						Aggregation.sort(pageable.getSort()),
						Aggregation.skip(pageable.getOffset()),
						Aggregation.limit(pageable.getPageSize())
					);
			
			AggregationResults<AuditContentItem> result = mongoTemplate.aggregate(agg, ActivityAudit.class, AuditContentItem.class);
			List<AuditContentItem> mappedResults = result.getMappedResults();
		
			if (CollectionUtil.isNotEmpty(mappedResults)) {
				
				List<ContentDataRecord> dataRecords = dataMgr.getContentRecords(mappedResults, 
					(actAuditItem) -> actAuditItem, dataView);
				
				dataPage = new PageImpl<>(dataRecords, pageable, count.getTotal());
			}
		}
		
		return dataPage;
	}

	@Override
	public Type resolverPackType() {
		return Type.RECENT_ACCESS_CONTENT;
	}
	
	public static class AuditCountResult {
		private long total;

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}
		
		
	}
	
	public static class AuditAggResult {
		private long total;
		private List<AuditContentItem> results;
		public long getTotal() {
			return total;
		}
		public void setTotal(long total) {
			this.total = total;
		}
		public List<AuditContentItem> getResults() {
			return results;
		}
		public void setResults(List<AuditContentItem> results) {
			this.results = results;
		}
		
	}
	
	public static class AuditContentItem implements ContentRecordRef {
		private String id;
		private String itemTitle;
		private String containerQId;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getItemTitle() {
			return itemTitle;
		}
		public void setItemTitle(String itemTitle) {
			this.itemTitle = itemTitle;
		}
		public String getContainerQId() {
			return containerQId;
		}
		public void setContainerQId(String containerQId) {
			this.containerQId = containerQId;
		}
		
		@Override
		public String getContentQId() {
			return getContainerQId();
		}
		@Override
		public String getRecordIdentity() {
			return getId();
		}
	}

}
