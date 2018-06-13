package com.enablix.app.content.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocEmbedHtmlResolver;
import com.enablix.app.content.doc.DocEmbedHtmlResolverFactory;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class DocEmbedHtmlTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocEmbedHtmlTask.class);
	
	@Value("${doc.embed.html.task.match.filename}")
	private String docMatchRegex;
	
	@Autowired
	private GenericDao dao;
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private DocEmbedHtmlResolverFactory embedHtmlResolverFactory;
	
	@Override
	public void run(TaskContext context) {
		
		Criteria queryCriteria = Criteria.where("name").regex(docMatchRegex)
				.andOperator(Criteria.where("embedHtml").exists(false)
								.orOperator(Criteria.where("embedHtml").is(null)));

		List<DocumentMetadata> docs = dao.findByCriteria(queryCriteria, DocumentMetadata.class, MongoDataView.ALL_DATA);
		
		for (DocumentMetadata docMd : docs) {
			
			try {
				
				if (docManager.checkReferenceRecordExists(docMd)) {

					DocEmbedHtmlResolver resolver = embedHtmlResolverFactory.getResolver(docMd);
					
					if (resolver != null) {
					
						String embedHtml = resolver.getEmbedHtml(docMd);
						if (StringUtil.hasText(embedHtml)) {
							docManager.updateEmbedHtml(docMd.getIdentity(), embedHtml);
						}
					}
				}
				
			} catch (Throwable e) {
				LOGGER.error("Error generating embed html for document [" + docMd.getIdentity() + "]", e);
			}
		}
	}

	@Override
	public String taskId() {
		return "doc-embed-html-task";
	}

}
