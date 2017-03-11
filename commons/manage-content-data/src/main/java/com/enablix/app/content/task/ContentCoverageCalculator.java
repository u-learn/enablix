package com.enablix.app.content.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.summary.repo.ContentCoverageRepository;
import com.enablix.app.content.task.coverage.ContentItemCoverageResolver;
import com.enablix.app.content.task.coverage.LinkedContainerCoverageResolver;
import com.enablix.app.content.task.coverage.SubContainerCoverageResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.summary.ContentCoverage;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.BoolFilter;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;
import com.enablix.services.util.template.walker.ContainerFilter;
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
	private ContentCoverageRepository contentCoverageRepo;
	
	@Autowired
	private GenericDao genericDao;
	
	@Override
	public void run(TaskContext context) {
		
		final Date currentDate = Calendar.getInstance().getTime();
		
		final String templateId = ProcessContext.get().getTemplateId();
		final TemplateWrapper template = templateManager.getTemplateWrapper(templateId);

		updateLastestFlagOfOldRecords();
		
		TemplateContainerWalker walker = new TemplateContainerWalker(template.getTemplate(),
				new ContainerFilter() {
					
					@Override
					public boolean accept(ContainerType container) {
						return !container.isRefData() && !TemplateUtil.isLinkedContainer(container);
					}
					
				});
		
		walker.walk(new ContainerVisitor() {
			
			@Override
			public void visit(ContainerType container) {
				
				String containerQId = container.getQualifiedId();
				String collectionName = template.getCollectionName(containerQId);
				
				List<Map<String, Object>> allRecords = crud.findAllRecord(collectionName);
				
				for (Map<String, Object> record : allRecords) {
					
					String title = (String) record.get(ContentDataConstants.CONTENT_TITLE_KEY);
					String identity = (String) record.get(ContentDataConstants.IDENTITY_KEY);
					
					ContentCoverage contentCoverage = new ContentCoverage();
					contentCoverage.setContentQId(containerQId);
					contentCoverage.setAsOfDate(currentDate);
					contentCoverage.setLatest(true);
					contentCoverage.setRecordIdentity(identity);
					contentCoverage.setRecordTitle(title);
					
					ContentDataRecord dataRecord = new ContentDataRecord(templateId, containerQId, record);
					
					for (ContentItemType itemType : container.getContentItem()) {
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
					
					LOGGER.debug("Content Coverage = {}", contentCoverage);
					contentCoverageRepo.save(contentCoverage);
				}
			}
		});
		
	}

	private void updateLastestFlagOfOldRecords() {
		
		BoolFilter latestTrue = new BoolFilter("latest", Boolean.TRUE, ConditionOperator.EQ);
		Query query = new Query(latestTrue.toPredicate(new Criteria()));
		
		Update update = new Update();
		update.set("latest", Boolean.FALSE);
		
		genericDao.updateMulti(query, update, ContentCoverage.class);
		
	}

	@Override
	public String taskId() {
		return "content-coverage-calculator";
	}

}
