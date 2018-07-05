package com.enablix.app.content.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.enrich.ContentUrlEnricher;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.content.EmbeddedUrl;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.TemplateContainerWalker;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class ContentEmddedUrlsTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentEmddedUrlsTask.class);
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentCrudService crud;
	
	@Autowired
	private ContentUrlEnricher contentUrlEnricher;
	
	@Autowired
	private GenericDao genericDao;
	
	@Override
	public void run(TaskContext context) {
		
		final Date currentDate = Calendar.getInstance().getTime();
		
		LOGGER.info("Running content embedded url updater task at [{}]", currentDate);
		
		final String templateId = ProcessContext.get().getTemplateId();
		final TemplateFacade template = templateManager.getTemplateFacade(templateId);

		TemplateContainerWalker walker = new TemplateContainerWalker(template.getTemplate(),
				(container) -> !container.isRefData() && !TemplateUtil.isLinkedContainer(container));
		
		walker.walk((container) -> {
				
				String containerQId = container.getQualifiedId();
				String collectionName = template.getCollectionName(containerQId);
				
				Pageable pageRequest = new PageRequest(0, 20);
				
				Page<Map<String, Object>> allRecords = 
						crud.findAllRecord(collectionName, pageRequest, MongoDataView.ALL_DATA);
				
				while (allRecords.hasContent()) {
					
					for (Map<String, Object> record : allRecords) {
						
						List<EmbeddedUrl> embeddedUrls = contentUrlEnricher.findEmbeddedUrls(containerQId, record, template);
						
						if (CollectionUtil.isNotEmpty(embeddedUrls)) {
							
							String recordId = ContentDataUtil.getRecordId(record);
							
							StringFilter recIdFilter = new StringFilter(
									ContentDataConstants.RECORD_ID_KEY , recordId, ConditionOperator.EQ);
							
							Query query = new Query(recIdFilter.toPredicate(new Criteria()));
				
							Update update = new Update();
							update.set(ContentDataConstants.CONTENT_URLS_KEY, embeddedUrls);
				
							genericDao.updateFirst(query, update, collectionName);
						}
					}
					
					pageRequest = pageRequest.next();
					allRecords = crud.findAllRecord(collectionName, pageRequest, MongoDataView.ALL_DATA);
				}
				
			}
		);
		
	}

	@Override
	public String taskId() {
		return "content-embedded-urls-update-task";
	}

}
