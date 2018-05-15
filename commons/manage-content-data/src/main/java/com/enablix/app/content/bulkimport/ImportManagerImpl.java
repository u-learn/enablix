package com.enablix.app.content.bulkimport;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.bulkimport.ImportProcessor.ImportDoc;
import com.enablix.app.content.bulkimport.ImportProcessor.ImportProcessedInfo;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.ErrorCodes;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.domain.content.ImportRecord;
import com.enablix.core.domain.content.ImportRequest;
import com.enablix.core.domain.content.ImportStatus;
import com.enablix.core.mongo.content.repo.ImportRequestRepository;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventSubscription;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.TemplateUtil;

@Component
public class ImportManagerImpl implements ImportManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportManagerImpl.class);
	
	@Autowired
	private ImportProcessorFactory processorFactory;
	
	@Autowired
	private ImportRequestRepository repo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private DocumentManager docMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void acceptRequest(ImportRequest request) throws ValidationException {
		
		ImportProcessor processor = processorFactory.getProcessor(request.getSource());
		
		if (processor == null) {
			throw new ValidationException(Collections.singletonList(
					new AppError(ErrorCodes.UNSUPPORTED_IMPORT_SOURCE, 
							"Unsupported Import Source [" + request.getSource() + "]")));
		}
		
		processor.validateRequest(request);
		
		request = repo.save(request);
		
		EventUtil.publishEvent(new Event<String>(Events.BULK_IMPORT_REQUEST, request.getIdentity()));
		
	}
	
	@EventSubscription(eventName = Events.BULK_IMPORT_REQUEST)
	public void importRecords(String requestIdentity) {
		
		ImportRequest request = repo.findByIdentityAndStatus(requestIdentity, ImportStatus.PENDING);
		
		if (request == null) {
			LOGGER.error("No pending Import Request not found for identity [{}]", requestIdentity);
			throw new IllegalArgumentException("Invalid Import Request identity [" + requestIdentity + "]");
		}
		
		ImportProcessor processor = processorFactory.getProcessor(request.getSource());
		if (processor == null) {
			throw new IllegalArgumentException("Unsupported Import Source [" + request.getSource() + "]");
		}
		
		String requestId = request.getId();
		request.setStatus(ImportStatus.STARTED);
		
		updateRequestStatus(requestId, ImportStatus.STARTED);
		
		ImportStatus finalStatus = ImportStatus.COMPLETED;
		
		ImportContext ctx = new ImportContext(request);
		
		for (ImportRecord rec : request.getRecords()) {
			
			if (rec.getStatus() == ImportStatus.PENDING) {
			
				String recordId = rec.getId();
				
				rec.setStatus(ImportStatus.STARTED);
				updateRecordStatus(requestId, recordId, ImportStatus.STARTED);
				
				ImportStatus recStatus = ImportStatus.COMPLETED;
				
				try {
					
					contentDataMgr.saveData(createContentSaveRequest(rec, ctx, processor));
					
				} catch (Throwable t) {
					
					LOGGER.error("Error processing record: " + rec, t);
					
					recStatus = ImportStatus.FAILED;
					finalStatus = ImportStatus.FAILED;
				}
				
				rec.setStatus(recStatus);
				updateRecordStatus(requestId, recordId, recStatus);
			}
		}
		
		request.setStatus(finalStatus);
		
		repo.save(request);
		
	}
	
	private UpdateContentRequest createContentSaveRequest(ImportRecord rec, ImportContext ctx,
			ImportProcessor processor) throws IOException {
		
		Map<String, Object> recordData = new HashMap<>();
		String templateId = ProcessContext.get().getTemplateId();
		
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		ContainerType container = template.getContainerDefinition(rec.getContentQId());
		
		String lblAttrId = template.getPortalLabelAttributeId(rec.getContentQId());
		recordData.put(lblAttrId, rec.getTitle());
		recordData.put(ContentDataConstants.EXTERNAL_SOURCE_ID_KEY, rec.getId());
		
		processTags(rec, ctx, container, recordData);
		processRecord(rec, ctx, processor, container, recordData);
		return new UpdateContentRequest(templateId, null, rec.getContentQId(), recordData);
	}

	private void processTags(ImportRecord rec, ImportContext ctx, ContainerType container,
			Map<String, Object> recordData) {

		if (CollectionUtil.isNotEmpty(rec.getTags())) {
			
			for (Map.Entry<String, Object> entry : rec.getTags().entrySet()) {

				ContentItemType boundedItem = TemplateUtil.findBoundedItemWithStoreId(container, entry.getKey());
				if (boundedItem != null) {
					recordData.put(boundedItem.getId(), entry.getValue());
				}
			}
		}
		
	}

	private void processRecord(ImportRecord rec, ImportContext ctx, ImportProcessor processor,
			ContainerType container, Map<String, Object> record) throws IOException {

		ImportProcessedInfo info = processor.processRecord(rec, container, ctx);
        
		if (info != null) {
			
			if (info.getImportDoc() != null) {
				
				ImportDoc importDoc = info.getImportDoc();
				
				if (importDoc.getInputStream() != null) {
					
					ContentItemType docItemType = TemplateUtil.getDocItemType(container);
					
					if (docItemType != null) {
						
						String docQId = docItemType.getQualifiedId();
						Document<DocumentMetadata> document = 
				        		docMgr.buildDocument(importDoc.getInputStream(), importDoc.getFilename(), 
				        				importDoc.getMimeType(), docQId, -1, null, false);
				        
				        DocumentMetadata docMd = docMgr.saveUsingContainerInfo(
				        		document, container.getQualifiedId(), null, false, false);
				        
				        ProcessContext pc = ProcessContext.get();
				        ActivityLogger.auditDocActivity(ActivityType.DOC_UPLOAD, docQId, null, 
				        		docMd.getIdentity(), Channel.SYSTEM, 
				        		new RegisteredActor(pc.getUserId(), pc.getUserDisplayName()), 
				        		null, null, null, null, importDoc.getFilename());
				        
				        // update the record with uploaded document info
				        record.put(docItemType.getId(), BeanUtil.beanToMap(docMd));
					}
				}
			}
			
			if (CollectionUtil.isNotEmpty(info.getRecordAttributes())) {
				record.putAll(info.getRecordAttributes());
			}
		}
	}

	private void updateRecordStatus(String requestId, String recordId, ImportStatus status) {
		Query query = Query.query(Criteria.where("id").is(requestId).and("records.id").is(recordId));
		Update set = new Update().set("records.$.status", status);
		mongoTemplate.updateFirst(query, set, ImportRequest.class);
	}
	
	private void updateRequestStatus(String requestId, ImportStatus status) {
		Query query = Query.query(Criteria.where("id").is(requestId));
		Update set = new Update().set("status", status);
		mongoTemplate.updateFirst(query, set, ImportRequest.class);
	}
	

}
