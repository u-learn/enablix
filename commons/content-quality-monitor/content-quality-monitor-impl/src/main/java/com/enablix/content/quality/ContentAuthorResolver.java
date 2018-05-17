package com.enablix.content.quality;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.services.util.ContentDataUtil;

@Component
public class ContentAuthorResolver {

	@Autowired
	private TemplateManager templateMgr;
	
	public Author getAuthor(Map<String, Object> record, String contentQId) {
		
		Author author = null;
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerDefinition(contentQId);
		
		Map<String, Object> docRecord = ContentDataUtil.findDocRecord(record, containerDef, template);
		
		if (CollectionUtil.isNotEmpty(docRecord)) {
			
			String authorUserId = ContentDataUtil.getContentModifiedBy(docRecord);
			
			if (StringUtil.hasText(authorUserId)) {
				String authorName = ContentDataUtil.getContentModifiedByName(docRecord);
				author = new Author(authorUserId, authorName);
			}
			
		}
		
		if (author == null) {
			String authorUserId = ContentDataUtil.getContentModifiedBy(record);
			String authorName = ContentDataUtil.getContentModifiedByName(record);
			
			author = new Author(authorUserId, authorName);
		}
		
		return author;
	}
	
	public static class Author {
		
		private String userId;
		private String name;
		
		public Author(String userId, String name) {
			super();
			this.userId = userId;
			this.name = name;
		}

		public String getUserId() {
			return userId;
		}
		
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		
	}
	
}
