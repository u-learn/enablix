package com.enablix.content.connection.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.CrudResponse;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.connection.AffectedContainerResolver;
import com.enablix.content.connection.ContentConnectionManager;
import com.enablix.content.connection.crud.ContentConnectionCrudService;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.activity.ContentConnActivity.ContentConnActivityType;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.services.util.ActivityLogger;
import com.enablix.commons.util.StringUtil;

@Component
public class ContentConnectionManagerImpl implements ContentConnectionManager {

	@Autowired
	private ContentConnectionCrudService crudService;
	
	@Autowired
	private AffectedContainerResolver affectedContainerResolver;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public CrudResponse<ContentTypeConnection> save(ContentTypeConnection contentConnection) {
		
		ContentConnActivityType activityType = StringUtil.isEmpty(contentConnection.getIdentity()) ? 
								ContentConnActivityType.ADDED : ContentConnActivityType.UPDATED;
		
		populateHoldingContainers(contentConnection);
		CrudResponse<ContentTypeConnection> response = crudService.saveOrUpdate(contentConnection);

		ActivityLogger.auditContentConnActivity(response.getPayload(), activityType);
		return response;
	}

	private void populateHoldingContainers(ContentTypeConnection contentConnection) {
		ContentTemplate template = templateManager.getTemplate(ProcessContext.get().getTemplateId());
		populateHoldingContainers(contentConnection, template);
	}
	
	private void populateHoldingContainers(ContentTypeConnection contentConnection, ContentTemplate template) {
		contentConnection.setHoldingContainers(
				affectedContainerResolver.findAffectedContainers(template, contentConnection));
	}
	
	public void refreshHoldingContainers(ContentTypeConnection contentConnection, ContentTemplate template) {
		populateHoldingContainers(contentConnection, template);
		crudService.getRepository().save(contentConnection);
	}
	
	@Override
	public void refreshHoldingContainersForAllConnections() {
		ContentTemplate template = templateManager.getTemplate(ProcessContext.get().getTemplateId());
		for (ContentTypeConnection contentConnection : crudService.getRepository().findAll()) {
			refreshHoldingContainers(contentConnection, template);
		}
	}

	@Override
	public ContentTypeConnection getContentConnection(String connectionIdentity) {
		return crudService.getRepository().findByIdentity(connectionIdentity);
	}

	@Override
	public void deleteContentConnection(String connectionIdentity) {
		
		ContentTypeConnection contentConn = crudService.getRepository().findByIdentity(connectionIdentity);
		if (contentConn == null) {
			throw new IllegalArgumentException("Invalid content connection identity [" + connectionIdentity + "]");
		}
		
		crudService.getRepository().deleteByIdentity(connectionIdentity);
		
		ActivityLogger.auditContentConnActivity(contentConn, ContentConnActivityType.DELETED);
	}

}
