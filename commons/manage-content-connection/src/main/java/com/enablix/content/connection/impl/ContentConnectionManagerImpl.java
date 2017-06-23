package com.enablix.content.connection.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.CrudResponse;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.connection.AffectedContainerResolver;
import com.enablix.content.connection.ContentConnectionManager;
import com.enablix.content.connection.crud.ContentConnectionCrudService;
import com.enablix.content.connection.web.ContentTypeLinkVO;
import com.enablix.content.connection.web.ContentValueLinkVO;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.core.domain.content.connection.ContentValueConnection;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;

@Component
public class ContentConnectionManagerImpl implements ContentConnectionManager {

	@Autowired
	private ContentConnectionCrudService crudService;
	
	@Autowired
	private AffectedContainerResolver affectedContainerResolver;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentCrudService contentCrud;
	
	@Override
	public CrudResponse<ContentTypeConnection> save(ContentTypeConnection contentConnection) {
		
		ActivityType activityType = StringUtil.isEmpty(contentConnection.getIdentity()) ? 
								ActivityType.ADDED : ActivityType.UPDATED;
		
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
		
		ActivityLogger.auditContentConnActivity(contentConn, ActivityType.DELETED);
	}

	@Override
	public List<ContentTypeLinkVO> getContentTypeLinkVO(String contentQId) {
		
		List<ContentTypeLinkVO> links = new ArrayList<>();
		
		List<ContentTypeConnection> connections = crudService.getRepository().findByContentQId(contentQId);
		
		if (connections != null) {
			
			TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
			String collectionName = template.getCollectionName(contentQId);
			
			List<Map<String, Object>> contentRecords = contentCrud.findAllRecord(collectionName, MongoDataView.ALL_DATA);
			
			Map<String, String> contentIdentityToTitle = new HashMap<>();
			
			contentRecords.forEach((record) -> {
				contentIdentityToTitle.put(ContentDataUtil.getRecordIdentity(record), 
						ContentDataUtil.findPortalLabelValue(record, template, contentQId, true));
			});
			
			for (ContentTypeConnection conn : connections) {
				
				ContentTypeLinkVO link = new ContentTypeLinkVO();
				link.setContentQId(contentQId);
				
				for (ContentValueConnection vc : conn.getConnections()) {
					
					String recIdentity = String.valueOf(vc.getContentValue());
					String recordTitle = contentIdentityToTitle.get(recIdentity);

					if (recordTitle != null) {
						link.addValueLink(new ContentValueLinkVO(recIdentity, recordTitle, vc.getConnectedContainers()));
					}
				}
				
				links.add(link);
			}
		}
		
		return links;
	}

}
