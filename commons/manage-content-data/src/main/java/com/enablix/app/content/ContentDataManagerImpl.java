package com.enablix.app.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.analytics.search.SearchClient;
import com.enablix.analytics.search.SearchInput;
import com.enablix.analytics.search.SearchInput.ParentFilter;
import com.enablix.app.content.delete.DeleteContentRequest;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataEventListenerRegistry;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.ContentIdentifier;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.quality.LevelBasedRuleSet;
import com.enablix.content.quality.QualityAnalyzer;
import com.enablix.content.quality.QualityRuleSet;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.ContentRecordGroup;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.api.ContentStackItem;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.api.SearchRequest.Pagination;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.content.event.ContentDataArchiveEvent;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.QualityAnalysis;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.mq.EventSubscription;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentDataManagerImpl implements ContentDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentDataManagerImpl.class);
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentCrudService crud;
	
	@Autowired
	private ContentDataEventListenerRegistry listenerRegistry;
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private QualityAnalyzer qualityAnalyzer;
	
	@Autowired
	private ContentPersistOperation persistor;
	
	@Autowired
	private QualityAlertProcessorFactory alertProcessorFactory;
	
	@Autowired
	private SearchClient searchClient;
	
	/*
	 * Use cases:
	 * 1. Insert top level container i.e. new record == Mongo Insert
	 * 2. Insert sub-level container i.e. new data in existing record == Mongo $addToSet
	 * 		(http://docs.mongodb.org/manual/reference/operator/update/addToSet/)
	 * 3. Update existing container record attributes == Mongo update $set
	 */
	@Override
	public UpdateContentResponse saveData(UpdateContentRequest request) {
		
		UpdateContentResponse response = new UpdateContentResponse();
		response.setContentRecord(request.getDataAsMap());
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		QualityAnalysis analysis = qualityAnalyzer.analyze(request.getDataAsMap(), 
				request.getContentQId(), getQualityRuleSet(request.getQualityAnalysisLevel()), template);
		
		response.setQualityAnalysis(analysis);
		
		if (analysis.hasAlerts()) {
			
			QualityAlertProcessor processor = alertProcessorFactory.getProcessor(request.getQualityAlertProcessing());
			response = processor.processQualityAlerts(analysis, request, persistor);
			
		} else {
			
			Map<String, Object> updatedRecord = persistor.persist(request);
			response.setContentRecord(updatedRecord);
		}
		
		return response;
	}
	
	private QualityRuleSet getQualityRuleSet(AlertLevel level) {
		return new LevelBasedRuleSet(new AlertLevel[] { level });
	}
	

	@Override
	public void deleteData(DeleteContentRequest request) {
		
		String contentQId = request.getContentQId();
		
		TemplateFacade templateWrapper = templateMgr.getTemplateFacade(request.getTemplateId());
		
		String collName = templateWrapper.getCollectionName(contentQId);
		
		ContainerType containerType = templateWrapper.getContainerDefinition(contentQId);
		
		try {
			if (TemplateUtil.hasOwnCollection(containerType)) {
				Map<String, Object> deletedRecord = crud.deleteRecord(collName, request.getRecordIdentity());
				
				if (deletedRecord != null) {
					String contentTitle = ContentDataUtil.findPortalLabelValue(deletedRecord, templateWrapper, contentQId);
					deleteRecordDocuments(deletedRecord, contentQId);
					publishContentDeleteEvent(new ContentDataDelEvent(request.getTemplateId(), 
						request.getContentQId(), request.getRecordIdentity(), containerType, contentTitle));
				}
				
			} else {
				String qIdRelativeToParent = QIdUtil.getElementId(contentQId);
				crud.deleteChild(collName, qIdRelativeToParent, request.getRecordIdentity());
			}
		
			deleteChildContainerData(templateWrapper, contentQId, request.getRecordIdentity());
			
		} catch (IOException ioe) {
			throw new RuntimeException("Error deleting record", ioe);
		}
	}
	
	private void publishContentDeleteEvent(ContentDataDelEvent event) {
		for (ContentDataEventListener listener : listenerRegistry.getListeners()) {
			listener.onContentDataDelete(event);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteChildContainerData(TemplateFacade templateWrapper, String containerQId, String recordIdentity) throws IOException {
		
		ContentTemplate template = templateWrapper.getTemplate();
		
		List<String> childContainerIds = TemplateUtil.getChildContainerIds(templateWrapper.getContainerDefinition(containerQId));
		for (String childContainerId : childContainerIds) {
			
			String childQId = QIdUtil.createQualifiedId(containerQId, childContainerId);
			ContainerType childContainer = templateWrapper.getContainerDefinition(childQId);
			
			if (TemplateUtil.hasOwnCollection(childContainer)) {
				
				String collName = templateWrapper.getCollectionName(childQId);
				List<HashMap> deletedChildRecords = 
						crud.deleteRecordsWithParentId(collName, recordIdentity);
				
				for (HashMap childRecord : deletedChildRecords) {

					String childRecordIdentity = (String) childRecord.get(ContentDataConstants.IDENTITY_KEY);
					String recordTitle = ContentDataUtil.findPortalLabelValue(childRecord, templateWrapper, childQId);
					
					deleteRecordDocuments(childRecord, childContainerId);
					publishContentDeleteEvent(new ContentDataDelEvent(template.getId(), 
							childQId, childRecordIdentity, childContainer, recordTitle));

					deleteChildContainerData(templateWrapper, childQId, childRecordIdentity);
				}
			}
		}
		
	}

	@Override
	public Object fetchDataJson(FetchContentRequest request, DataView dataView) {
		
		Object data = null;
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		String contentQId = request.getContentQId();
		
		TemplateFacade templateWrapper = templateMgr.getTemplateFacade(request.getTemplateId());
		ContentTemplate template = templateWrapper.getTemplate();
		
		// check for linked container
		ContainerType container = TemplateUtil.findContainer(template.getDataDefinition(), contentQId);
		
		String linkedContainerQId = container.getLinkContainerQId();
		if (!StringUtil.isEmpty(linkedContainerQId)) {
			contentQId = linkedContainerQId;
		}
		
		String collName = templateWrapper.getCollectionName(contentQId);
		String qIdRelativeToParent = TemplateUtil.getQIdRelativeToParentContainer(template, contentQId);
		
		if (!StringUtil.isEmpty(linkedContainerQId)) {
			
			if (!StringUtil.isEmpty(request.getParentRecordIdentity())) {
				
				data = request.getPageable() == null ? crud.findAllRecordWithLinkContainerId(collName, 
							container.getLinkContentItemId(), request.getParentRecordIdentity(), view)
						: crud.findAllRecordWithLinkContainerId(collName, 
								container.getLinkContentItemId(), request.getParentRecordIdentity(), 
								request.getPageable(), view);
				
			} else if (!StringUtil.isEmpty(request.getRecordIdentity())) {
				// Fetch one record
				data = crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity(), view);
			} 				
			
			
		} else if (StringUtil.isEmpty(request.getParentRecordIdentity())
				&& StringUtil.isEmpty(request.getRecordIdentity())) {
			
			// Fetch all root elements for the template
			data = request.getPageable() == null ? crud.findAllRecord(collName, view) : 
						crud.findAllRecord(collName, request.getPageable(), view);
			
		} else if (!StringUtil.isEmpty(request.getParentRecordIdentity())) {
			
			// Fetch all child containers
			if (TemplateUtil.hasOwnCollection(templateWrapper.getContainerDefinition(contentQId))) {
				// content is in its own collection, hence query with parent id
				data = request.getPageable() == null ? crud.findAllRecordWithParentId(collName, request.getParentRecordIdentity(), view)
						: crud.findAllRecordWithParentId(collName, request.getParentRecordIdentity(), request.getPageable(), view);
				
			} else {
				// content is a child array in parents collection, hence retrieve child elements
				data = request.getPageable() == null ? crud.findChildElements(collName, 
										qIdRelativeToParent, request.getParentRecordIdentity(), view)
						: crud.findChildElements(collName, qIdRelativeToParent, request.getParentRecordIdentity(), 
										request.getPageable(), view);
			}
			
		} else if (!StringUtil.isEmpty(request.getRecordIdentity())) {
			// Fetch one record
			data = crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity(), view);
		} 
		
		return data;
	}

	@Override
	public List<Map<String, Object>> fetchPeers(FetchContentRequest request, DataView dataView) {
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		String contentQId = request.getContentQId();
		
		TemplateFacade templateWrapper = templateMgr.getTemplateFacade(request.getTemplateId());
		ContentTemplate template = templateWrapper.getTemplate();
		
		String collName = templateWrapper.getCollectionName(contentQId);
		String qIdRelativeToParent = TemplateUtil.getQIdRelativeToParentContainer(template, contentQId);

		List<Map<String, Object>> peers = new ArrayList<Map<String, Object>>();
		
		// Fetch one record
		Map<String, Object> recordData = 
				crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity(), view);
		
		if (recordData != null) {
			// Fetch all child containers
			if (TemplateUtil.hasOwnCollection(templateWrapper.getContainerDefinition(contentQId))) {
				
				String parentIdentity = ContentDataUtil.findParentIdentityFromAssociation(recordData);
				
				// content is in its own collection, hence query with parent id
				List<Map<String, Object>> allPeers = crud.findAllRecordWithParentId(
											collName, parentIdentity, view);
				
				// remove current
				for (Map<String, Object> item : allPeers) {
					String peerIdentity = (String) item.get(ContentDataConstants.IDENTITY_KEY);
					if (!request.getRecordIdentity().equals(peerIdentity)) {
						peers.add(item);
					}
				}
				
			} else {
				// TODO:
			}
		}
	
		return peers == null ? new ArrayList<Map<String, Object>>() : peers;
	}
	
	@Override
	public Map<String, Object> fetchParentRecord(TemplateFacade template, 
			String recordQId, Map<String, Object> record, DataView dataView) {
		
		Map<String, Object> parentRecord = null;
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		String parentQId = QIdUtil.getParentQId(recordQId);
		
		String parentIdentity = ContentDataUtil.findParentIdentityFromAssociation(record);
		
		if (!StringUtil.isEmpty(parentIdentity)) {
		
			// check if data is self contained or in parent container
			String collName = template.getCollectionName(parentQId);
			ContainerType parentContainer = template.getContainerDefinition(parentQId);
			
			if (TemplateUtil.hasOwnCollection(parentContainer)) {
				parentRecord = crud.findRecord(collName, parentIdentity, view);
				
			} else {
				record = crud.findRecord(collName, QIdUtil.getElementId(parentQId), parentIdentity, view);
			}
		}
		
		return parentRecord;
	}


	@Override
	public Map<String, Object> getContentRecord(ContentRecordRef dataRef, TemplateFacade template, DataView dataView) {
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		String collName = template.getCollectionName(dataRef.getContentQId());
		
		Map<String, Object> triggerItemRecord = StringUtil.hasText(collName) ? 
				crud.findRecord(collName, dataRef.getRecordIdentity(), view) : null;

		return triggerItemRecord;
	}


	@Override
	public List<Map<String, Object>> getContentRecords(String containerQId, List<String> recordIdentities,
			TemplateFacade template, DataView dataView) {
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		
		String collName = template.getCollectionName(containerQId);
		return crud.findRecords(collName, recordIdentities, view);
	}
	
	@Override
	public List<ContentDataRecord> getContentStackRecords(List<ContentStackItem> contentStackItems, DataView view) {

		String templateId = ProcessContext.get().getTemplateId();
		
		// group by containerQId so that we can reduce the number of DB queries
		Map<String, List<String>> containerIdentities = groupContentIdentitiesByContainerQId(contentStackItems);

		List<ContentRecordGroup> contentStackGroup = fetchContentStackRecords(containerIdentities, null, view);
		List<ContentDataRecord> stackRecords = new ArrayList<>();
		
		for (ContentRecordGroup recGrp : contentStackGroup) {
			
			for (Map<String, Object> rec : recGrp.getRecords()) {
				stackRecords.add(new ContentDataRecord(templateId, recGrp.getContentQId(), rec));
			}
		}
		
		return stackRecords;
	}


	private List<ContentRecordGroup> fetchContentStackRecords(
			Map<String, List<String>> containerIdentities, Pageable pageable, DataView dataView) {
		
		List<ContentRecordGroup> contentGroups = new ArrayList<>();
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade templateWrapper = templateMgr.getTemplateFacade(templateId);
		
		// iterate for each container type and fetch corresponding records
		for (Map.Entry<String, List<String>> entry : containerIdentities.entrySet()) {
		
			String containerQId = entry.getKey();
			String collectionName = templateWrapper.getCollectionName(containerQId);
			
			if (!StringUtil.isEmpty(collectionName)) {
				
				Page<Map<String, Object>> dataRecords = crud.findRecords(collectionName, entry.getValue(), pageable, view);
				
				if (CollectionUtil.isNotEmpty(dataRecords.getContent())) {
					
					ContentRecordGroup recGrp = new ContentRecordGroup();
					recGrp.setContentQId(containerQId);
					recGrp.setRecords(dataRecords);
					
					contentGroups.add(recGrp);
				}
				
			} else {
				LOGGER.warn("Collection name not resolved for [{}] container", containerQId);
			}
		}
			
		return contentGroups;
	}
	
	private Map<String, List<String>> groupContentIdentitiesByContainerQId(List<ContentStackItem> contentStackItems) {
		
		Map<String, List<String>> containerIdentities = new LinkedHashMap<>();
		
		for (ContentStackItem ref : contentStackItems) {
			checkAndAddIdentityForContainer(containerIdentities, ref.getQualifiedId(), ref.getIdentity());
		}
		
		return containerIdentities;
	}


	private void checkAndAddIdentityForContainer(
			Map<String, List<String>> containerIdentities, String qualifiedId, String identity) {
		
		List<String> identities = containerIdentities.get(qualifiedId);
		
		if (identities == null) {
			identities = new ArrayList<>();
			containerIdentities.put(qualifiedId, identities);
		}
		
		identities.add(identity);
	}


	@Override
	public List<ContentRecordGroup> getContentStackForContentRecord(
			String containerQId, String instanceIdentity, Pageable pageable, DataView dataView) {
		
		return fetchContentStackData(containerQId, instanceIdentity, null, pageable, dataView);
	}


	private List<ContentRecordGroup> fetchContentStackData(String containerQId, String instanceIdentity,
			List<String> filterItemQId, Pageable pageable, DataView dataView) {
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		String collectionName = template.getCollectionName(containerQId);
		
		List<ContentRecordGroup> stackRecords = null;
		
		Map<String, Object> contentRecord = crud.findRecord(collectionName, instanceIdentity, view);
		
		if (CollectionUtil.isNotEmpty(contentRecord)) {
			
			Map<String, List<String>> containerIdentities = new LinkedHashMap<>();
			ContainerType container = template.getContainerDefinition(containerQId);
			
			for (ContentItemType itemType : container.getContentItem()) {
			
				if (itemType.getType() == ContentItemClassType.CONTENT_STACK) {
					
					@SuppressWarnings("unchecked")
					List<Map<String, String>> contentStackAttrVal = 
							(List<Map<String, String>>) contentRecord.get(itemType.getId());
					
					addToContainerIdGroup(filterItemQId, containerIdentities, contentStackAttrVal);
					
				}
			}
			
			// fetch content data records
			stackRecords = fetchContentStackRecords(containerIdentities, pageable, dataView);
		}
		
		return stackRecords == null ? new ArrayList<ContentRecordGroup>() : stackRecords;
	}

	private void addToContainerIdGroup(List<String> filterItemQId, Map<String, List<String>> containerIdentities,
			List<Map<String, String>> contentStackAttrVal) {
		
		if (CollectionUtil.isNotEmpty(contentStackAttrVal)) {
			
			for (Map<String, String> contentStackEntry : contentStackAttrVal) {
				
				String qualifiedId = contentStackEntry.get(ContentDataConstants.QUALIFIED_ID_KEY);
				
				if (CollectionUtil.isEmpty(filterItemQId) || filterItemQId.contains(qualifiedId)) {
					String identity = contentStackEntry.get(ContentDataConstants.IDENTITY_KEY);
					checkAndAddIdentityForContainer(containerIdentities, qualifiedId, identity);
				}
				
			}
		}
	}
	
	@Override
	public List<ContentRecordGroup> fetchStackDetails(List<Map<String, String>> stackValue, Pageable pageable, DataView dataView) {
		
		Map<String, List<String>> containerIdentities = new LinkedHashMap<>();
		addToContainerIdGroup(null, containerIdentities, stackValue);
		
		// fetch content data records
		List<ContentRecordGroup> stackRecords = fetchContentStackRecords(containerIdentities, pageable, dataView);
		return stackRecords == null ? new ArrayList<ContentRecordGroup>() : stackRecords;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List<ContentRecordGroup> fetchAllChildrenData(String parentQId, String parentIdentity, 
			Pageable pageable, List<String> childQIds, String textQuery, DataView view) {
		
		List<ContentRecordGroup> contentGroups = new ArrayList<>();
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		ContainerType parentContainer = template.getContainerDefinition(parentQId);
		if (parentContainer != null) {
			
			for (ContainerType childContainer : parentContainer.getContainer()) {
				
				String childQId = childContainer.getQualifiedId();
				
				String concreteQId = TemplateUtil.isLinkedContainer(childContainer) ? 
						childContainer.getLinkContainerQId() : childContainer.getQualifiedId();
						
				if (CollectionUtil.isEmpty(childQIds) || childQIds.contains(concreteQId)) {
					
					Object data = null;
					
					if (StringUtil.hasText(textQuery)) {
						
						SearchInput input = new SearchInput();
						input.setContentQIds(Collections.singletonList(concreteQId));
						
						ParentFilter parent = new ParentFilter();
						parent.setIdentity(parentIdentity);
						parent.setQualifiedId(parentQId);
						
						input.setParent(parent);
						
						SearchRequest request = new SearchRequest();
						request.setTextQuery(textQuery);
						
						Pagination pagination = new Pagination();
						pagination.setPageNum(pageable.getPageNumber());
						pagination.setPageSize(pageable.getPageSize());

						request.setPagination(pagination);
						input.setRequest(request );
						
						data = searchData(input, template, view);
						
					} else {
						
						FetchContentRequest request = new FetchContentRequest(
								templateId, childQId, parentIdentity, null, pageable);
						
						data = fetchDataJson(request, view);
					}
					
					ContentRecordGroup contentGroup = new ContentRecordGroup();
					contentGroup.setContentQId(childQId);
					
					if (data instanceof Page) {
						
						contentGroup.setRecords((Page) data);
						
					} else if (data instanceof List) {
						
						contentGroup.setRecords((List) data);
						
					} else if (data instanceof Map) {
						
						contentGroup.setRecords((Map) data);
					}
					
					if (CollectionUtil.isNotEmpty(contentGroup.getRecords())) {
						contentGroups.add(contentGroup);
					}
				}
			}
			
		} else {
			LOGGER.warn("Invalid container qualified id [{}]", parentQId);
		}
		
		return contentGroups;
	}
	
	private Object searchData(SearchInput input, TemplateFacade template, DataView dataView) {
		return searchClient.searchTypeRecords(input, template, dataView);
	}

	@Override
	public List<ContentRecordGroup> fetchRecordAndChildData(String contentQId, String contentIdentity, 
			Pageable childPagination, List<String> childQIds, String textQuery, DataView view) {
		
		List<ContentRecordGroup> contentGroups = new ArrayList<>();
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		ContentDataRef dataRef = ContentDataRef.createContentRef(templateId, contentQId, contentIdentity, null);
		
		Map<String, Object> contentRecord = getContentRecord(dataRef, template, view);
		
		if (CollectionUtil.isNotEmpty(contentRecord)) {
		
			ContentRecordGroup contentGroup = new ContentRecordGroup();
			
			contentGroup.setContentQId(contentQId);
			contentGroup.setRecords(contentRecord);
			
			contentGroups.add(contentGroup);
			
			// add child data
			contentGroups.addAll(fetchAllChildrenData(contentQId, contentIdentity, childPagination, childQIds, textQuery, view));
		}
		
		return contentGroups;
	}


	@Override
	public List<ContentRecordGroup> getContentStackItemForContentRecord(String containerQId, String instanceIdentity,
			String itemQId, Pageable pageable, DataView userDataView) {
		
		List<String> filterItemQId = new ArrayList<>();
		filterItemQId.add(itemQId);
		
		return fetchContentStackData(containerQId, instanceIdentity, filterItemQId, pageable, userDataView);
	}


	@Override
	public Map<String, Object> getContentRecordByDocRef(String docIdentity, String docContentQId, DataView view) {

		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		String containerQId = QIdUtil.getParentQId(docContentQId);
		String collectionName = template.getCollectionName(containerQId);
		String docAttrId = QIdUtil.getElementId(docContentQId);
		
		return crud.findRecordByDocIdentity(collectionName, docAttrId, docIdentity, DataViewUtil.getMongoDataView(view));
	}


	
	@Override
	public void deleteRecordDocuments(Map<String, Object> record, String containerQId) throws IOException {
		processRecordDocuments(record, containerQId, (docMd) -> { 
			DocumentMetadata updatedDocMd = docManager.delete(docMd); 
			return updatedDocMd; 
		});
	}

	@SuppressWarnings("rawtypes")
	private void processRecordDocuments(Map<String, Object> record, String containerQId, 
			DocumentProcessor process) throws IOException {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerDefinition(containerQId);
		
		if (containerDef != null) {
		
			for (ContentItemType itemType : containerDef.getContentItem()) {
			
				if (itemType.getType() == ContentItemClassType.DOC) {
					
					Object docMd = record.get(itemType.getId());
				
					if (docMd != null && docMd instanceof Map) {
						
						String docIdentity = (String) ((Map) docMd).get(ContentDataConstants.IDENTITY_KEY);
						DocumentMetadata docMetadata = docManager.getDocumentMetadata(docIdentity);
						
						if (docMetadata != null) {
							process.process(docMetadata);
						}
					}
				}
			}
		}
	}
	
	@EventSubscription(eventName = Events.CONTAINER_DEF_REMOVED)
	public void deleteRecords(ContainerType container) {
		// remove all records on container deletions
		if (container != null && container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_DIMENSION) {
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			String collectionName = template.getCollectionName(container.getQualifiedId());
			
			List<Map<String, Object>> records = crud.findAllRecord(collectionName, MongoDataView.ALL_DATA);
			
			records.forEach((rec) -> {
				DeleteContentRequest deleteRequest = new DeleteContentRequest(template.getId(), 
						container.getQualifiedId(), ContentDataUtil.getRecordIdentity(rec));
				deleteData(deleteRequest);
			});
			
		}
	}
	
	private static interface DocumentProcessor {
		DocumentMetadata process(DocumentMetadata docMd) throws IOException;
	}

	@Override
	public <T> List<ContentDataRecord> getContentRecords(Iterable<T> itr, 
			Function<T, ContentRecordRef> dataRefTx, DataView view) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		List<ContentDataRecord> dataRecords = new ArrayList<>();
		
		for (T content : itr) {
			
			ContentRecordRef dataRef = dataRefTx.apply(content);
			
			if (dataRef != null) {
			
				Map<String, Object> contentRecord = getContentRecord(dataRef, template, view);
				
				if (CollectionUtil.isNotEmpty(contentRecord)) {
					dataRecords.add(new ContentDataRecord(
						template.getId(), dataRef.getContentQId(), contentRecord));
				}
			}
		}
		
		return dataRecords;
	}

	@Override
	public boolean archiveContent(ContentIdentifier request) {
		return updateArchiveStatus(request, true);
	}

	@Override
	public boolean unarchiveContent(ContentIdentifier request) {
		return updateArchiveStatus(request, false);
	}

	private boolean updateArchiveStatus(ContentIdentifier request, boolean archiveStatus) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		String collectionName = template.getCollectionName(request.getContentQId());
		
		boolean success = false;
		
		
		if (StringUtil.hasText(collectionName)) {
			
			success = archiveStatus ? crud.archiveRecord(collectionName, request.getRecordIdentity())
					: crud.unarchiveRecord(collectionName, request.getRecordIdentity());
			
			if (success) {
			
				publishContentArchiveEvent(new ContentDataArchiveEvent(ProcessContext.get().getTemplateId(), 
					request.getContentQId(), request.getRecordIdentity(), 
					com.enablix.core.domain.activity.ContentActivity.ContainerType.CONTENT, archiveStatus));
			}
		}
		
		return success;
	}

	private void publishContentArchiveEvent(ContentDataArchiveEvent event) {
		for (ContentDataEventListener listener : listenerRegistry.getListeners()) {
			listener.onContentDataArchive(event);
		}
	}
}
