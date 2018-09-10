package com.enablix.app.content.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.summary.repo.ContentCoverageRepository;
import com.enablix.app.content.task.coverage.ContentItemCoverageResolver;
import com.enablix.app.content.task.coverage.ContentStackCoverageResolver;
import com.enablix.app.content.task.coverage.LinkedContainerCoverageResolver;
import com.enablix.app.content.task.coverage.SubContainerCoverageResolver;
import com.enablix.app.report.util.ReportUtil;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.summary.ContentCoverage;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.ContainerVisitor;
import com.enablix.services.util.template.walker.TemplateContainerWalker;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class ContentCoverageCalculator implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentCoverageCalculator.class);
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentCrudService crud;
	
	@Autowired
	private ContentItemCoverageResolver contentItemCoverageResolver;
	
	@Autowired
	private SubContainerCoverageResolver subContCoverageResolver;
	
	@Autowired
	private LinkedContainerCoverageResolver linkedContCoverageResolver;
	
	@Autowired
	private ContentStackCoverageResolver contentStackCoverageResolver;
	
	@Autowired
	private ContentCoverageRepository contentCoverageRepo;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@Override
	public void run(TaskContext context) {
		
		final Date currentDate = Calendar.getInstance().getTime();
		
		LOGGER.info("Running content coverage task at [{}]", currentDate);
		
		final String templateId = ProcessContext.get().getTemplateId();
		final TemplateFacade template = templateManager.getTemplateFacade(templateId);

		if (template == null) {
			LOGGER.error("Template [{}] not found for tenant [{}]", templateId, ProcessContext.get().getTenantId());
			return;
		}
		
		reportUtil.updateLastestFlagOfOldRecords(ContentCoverage.class);
		
		TemplateContainerWalker walker = new TemplateContainerWalker(template.getTemplate(),
				(container) -> !container.isRefData() && !TemplateUtil.isLinkedContainer(container));
		
		walker.walk(new ContainerVisitor() {
			
			@Override
			public void visit(ContainerType container) {
				
				String containerQId = container.getQualifiedId();
				String collectionName = template.getCollectionName(containerQId);
				
				List<Map<String, Object>> allRecords = crud.findAllRecord(collectionName, MongoDataView.ALL_DATA);
				
				for (Map<String, Object> record : allRecords) {
					
					String title = (String) record.get(ContentDataConstants.CONTENT_TITLE_KEY);
					String identity = (String) record.get(ContentDataConstants.IDENTITY_KEY);
					
					ContentCoverage contentCoverage = new ContentCoverage();
					contentCoverage.setContentQId(containerQId);
					contentCoverage.setContainerLabel(container.getLabel());
					contentCoverage.setAsOfDate(currentDate);
					contentCoverage.setLatest(true);
					contentCoverage.setRecordIdentity(identity);
					contentCoverage.setRecordTitle(title);
					contentCoverage.setRecordCreatedAt(ContentDataUtil.getContentCreatedAt(record));
					contentCoverage.setRecordCreatedBy(ContentDataUtil.getContentCreatedBy(record));
					contentCoverage.setRecordCreatedByName(ContentDataUtil.getContentCreatedByName(record));
					contentCoverage.setRecordModifiedAt(ContentDataUtil.getContentModifiedAt(record));
					contentCoverage.setRecordModifiedBy(ContentDataUtil.getContentModifiedBy(record));
					contentCoverage.setRecordModifiedByName(ContentDataUtil.getContentModifiedByName(record));
					
					ContentDataRecord dataRecord = new ContentDataRecord(templateId, containerQId, record);
					
					for (ContentItemType itemType : container.getContentItem()) {
						
						if (itemType.getType() == ContentItemClassType.CONTENT_STACK) {
						
							Map<String, Long> stackStats = contentStackCoverageResolver.getContentStats(itemType, record);
							if (CollectionUtil.isNotEmpty(stackStats)) {
								stackStats.entrySet().forEach((entry) -> contentCoverage.addCoverageStat(entry.getKey(), entry.getValue()));
							}
						}
						
						long count = contentItemCoverageResolver.getCount(itemType, record);
						contentCoverage.addCoverageStat(itemType.getQualifiedId(), count);
					}
					
					for (ContainerType subContainer : container.getContainer()) {
						
						long count = 0;
						
						if (TemplateUtil.isLinkedContainer(subContainer)) {
							count = linkedContCoverageResolver.getCount(subContainer, dataRecord, template);
						} else {
							count = subContCoverageResolver.getCount(subContainer, dataRecord, template);
						}
						
						contentCoverage.addCoverageStat(subContainer.getQualifiedId(), count);
					}
					
					contentCoverageRepo.save(contentCoverage);
				}
			}
		});
		
	}

	@Override
	public String taskId() {
		return "content-coverage-calculator";
	}

}
