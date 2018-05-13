package com.enablix.tenant.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.content.SampleContent;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.system.repo.SampleContentRepository;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.tenant.TenantSetupTask;

@Component
public class SampleContentCreator implements TenantSetupTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleContentCreator.class);
	
	@Value("${new.tenant.sample.content.file.location:#{'${baseDir}/config/sample-content'}}")
	private String sampleContentLocation;
	
	@Autowired
	private SampleContentRepository sampleContentRepo;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Override
	public void execute(Tenant tenant) throws Exception {
		
		List<SampleContent> sampleList = sampleContentRepo.findAll();
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		for (SampleContent content : sampleList) {
			
			String contentQId = content.getContentQId();
			Map<String, Object> record = content.getRecord();
			String title = ContentDataUtil.findPortalLabelValue(record,template, contentQId);
			
			ContainerType container = template.getContainerDefinition(contentQId);
			
			if (container != null) {
			
				try {
					
					findAndUploadDoc(record, title, container);
					
					dataMgr.saveData(new UpdateContentRequest(template.getId(), null, contentQId, record) {
						
						/**
						 * Overriding the update check as the sample record will contain identity 
						 * as we need the identity to refer it in other sample records but the
						 * request will always be to insert a new record.
						 */
						@Override
						public boolean isUpdateAttribRequest() {
							return false;
						}
					});
					
				} catch (IOException e) {
					LOGGER.error("Error uploading file", e);
				}
				
			} else {
				LOGGER.error("Invalid sample content qualified id: {}", contentQId);
			}
		}
	}

	private void findAndUploadDoc(Map<String, Object> record, String title, ContainerType container)
			throws FileNotFoundException, IOException {
		
		ContentItemType docItemType = TemplateUtil.getDocItemType(container);
		
		if (docItemType != null) {
		
			Object fileObj = record.get(docItemType.getId());
			
			if (fileObj != null) {
				
				@SuppressWarnings("unchecked")
				Map<String, Object> fileInfo = (Map<String, Object>) fileObj;
				
				String filename = (String) fileInfo.get("location");
				String contentType = (String) fileInfo.get("contentType");
				
				Boolean generatePreview = (Boolean) fileInfo.get("generatePreview");
				generatePreview = generatePreview == null ? Boolean.FALSE : generatePreview;
				
				String absFilePath = sampleContentLocation + File.separator + filename;
				String docQId = docItemType.getQualifiedId();
				String contentIdentity = ContentDataUtil.getRecordIdentity(record);
				
				File file = new File(absFilePath);
				if (!file.exists()) {
					LOGGER.error("File not found: {}. Ignoring corresponding sample record", absFilePath);
					throw new FileNotFoundException("File [" + absFilePath + "] not found");
				}

				
				FileInputStream fis = new FileInputStream(file);
		        
				Document<DocumentMetadata> document = 
		        		docManager.buildDocument(fis, file.getName(),
		        				contentType, docQId, file.length(), null, false);
		        
		        DocumentMetadata docMd = docManager.saveUsingContainerInfo(
		        		document, container.getQualifiedId(), null, generatePreview, true);
		        
		        ActivityLogger.auditDocActivity(ActivityType.DOC_UPLOAD, docQId, contentIdentity, 
		        		docMd.getIdentity(), Channel.SYSTEM, 
		        		new RegisteredActor(AppConstants.SYSTEM_USER_ID, AppConstants.SYSTEM_USER_NAME), 
		        		null, null, null, title, filename);
		        
		        // update the record with uploaded document info
		        record.put(docItemType.getId(), BeanUtil.beanToMap(docMd));

			}
		}
	}

	@Override
	public float executionOrder() {
		return TenantSetupTaskOrder.SAMPLE_CONTENT_SETUP;
	}
	
}
