package com.enablix.content.quality;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.quality.QualityAnalyzer.AnalysisRuleSet;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.TemplateContainerWalker;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class ContentQualityAnalysisTask implements Task {

	@Autowired
	private QualityAnalyzer analyzer;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentCrudService crud;
	
	@Override
	public void run(TaskContext context) {

		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		if (TemplateUtil.hasQualityCheckRules(template.getTemplate())) {
			
			TemplateContainerWalker walker = new TemplateContainerWalker(template.getTemplate());
			
			walker.walk((container) -> {
				
				String containerQId = container.getQualifiedId();
				String collectionName = template.getCollectionName(containerQId);
				
				Pageable pageRequest = new PageRequest(0, 20);
				
				Page<Map<String, Object>> allRecords = 
						crud.findAllRecord(collectionName, pageRequest, MongoDataView.ALL_DATA);
				
				while (allRecords.hasContent()) {
					
					for (Map<String, Object> record : allRecords) {
						analyzer.analyzeAndRecord(record, containerQId, AnalysisRuleSet.ALL, template);
					}
					
					pageRequest = pageRequest.next();
					allRecords = crud.findAllRecord(collectionName, pageRequest, MongoDataView.ALL_DATA);
				}
				
			});
		}
	}

	@Override
	public String taskId() {
		return "content-quality-analysis-task";
	}

}
