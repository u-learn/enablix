package com.enablix.app.audit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.activity.audit.ActivityContextAware;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.RecordReference;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.activity.Activity;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.DocumentActivity;
import com.enablix.core.domain.content.ContentChangeDelta;
import com.enablix.core.domain.content.ContentChangeDelta.AttributeChange;
import com.enablix.core.mongo.audit.repo.ActivityAuditRepository;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mq.EventSubscription;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class ActivityAuditor {

	@Autowired
	private ActivityAuditRepository auditRepo;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DocumentManager docMgr;
	
	@Autowired
	private GenericDao dao;
	
	@EventSubscription(eventName = {Events.AUDIT_ACITIVITY})
	public void auditActivity(ActivityAudit activity) {
		
		if (activity.getDateDimension() == null) {
			activity.setDateDimension(
					DateUtil.getDateDimension(activity.getActivityTime()));
		}
		
		updateRefererHost(activity);
		
		checkAndUpdateContentTitle(activity);
		
		checkAndUpdateDocRefRecord(activity);
		
		auditRepo.save(activity);
	}
	
	private void updateRefererHost(ActivityAudit activity) {
		
		if (activity.getActivity() instanceof ActivityContextAware) {
			
			ActivityContextAware ctxAware = (ActivityContextAware) activity.getActivity();
			String referer = ctxAware.getReferer();
			
			if (StringUtil.hasText(referer)) {
				
				try {
					URL url = new URL(referer);
					ctxAware.setRefererHost(url.getHost());
				} catch (MalformedURLException e) {
					// ignore
				}
			}
		}
		
	}

	private void checkAndUpdateContentTitle(ActivityAudit activity) {
		
		Activity auditActvy = activity.getActivity();
		
		if (auditActvy instanceof RecordReference) {
			
			RecordReference recordActivity = (RecordReference) auditActvy;
			
			String containerQId = recordActivity.getContainerQId();
			String itemIdentity = recordActivity.getItemIdentity();
			
			if (!StringUtil.hasText(recordActivity.getItemTitle())
					&& StringUtil.hasText(itemIdentity)
					&& StringUtil.hasText(containerQId)) {
				
				String templateId = ProcessContext.get().getTemplateId();
				TemplateFacade template = templateMgr.getTemplateFacade(templateId);

				ContentDataRef contentRef = ContentDataRef.createContentRef(templateId, containerQId, itemIdentity, null);
				
				Map<String, Object> contentRecord = contentDataMgr.getContentRecord(contentRef, template, DataViewUtil.allDataView());
				String title = (String) contentRecord.get(ContentDataConstants.CONTENT_TITLE_KEY);
				recordActivity.setItemTitle(title);
			}
		}
		
	}
	
	private void checkAndUpdateDocRefRecord(ActivityAudit activity) {
		
		Activity auditActvy = activity.getActivity();
		
		if (auditActvy instanceof DocumentActivity) {
			
			DocumentActivity docActivity = (DocumentActivity) auditActvy;
			
			String itemIdentity = docActivity.getItemIdentity();
			String itemTitle = docActivity.getItemTitle();
			
			if (!StringUtil.hasText(itemTitle) || !StringUtil.hasText(itemIdentity)) {
				
				Map<String, Object> refRecord = docMgr.getReferenceRecord(docActivity.getDocQId(), docActivity.getDocIdentity());
				
				if (refRecord != null) {

					itemTitle = (String) refRecord.get(ContentDataConstants.CONTENT_TITLE_KEY);
					itemIdentity = ContentDataUtil.getRecordIdentity(refRecord);
					
					docActivity.setItemIdentity(itemIdentity);
					docActivity.setItemTitle(itemTitle);
					
					if (!StringUtil.hasText(docActivity.getContainerQId())) {
						docActivity.setContainerQId(QIdUtil.getParentQId(docActivity.getDocQId()));
					}
					
				}
			}
		}
	}
	
	@EventSubscription(eventName = {Events.CONTENT_CHANGE_EVENT})
	public void checkAndUpdateDocUploadAudit(ContentDataSaveEvent event) {
		
		ContainerType containerDef = event.getContainerType();
		ContentItemType docItemType = TemplateUtil.getDocItemType(containerDef);
		
		if (docItemType != null) {
			
			ContentChangeDelta changeDelta = event.getChangeDelta();
			
			if (changeDelta != null) {
				
				AttributeChange attributeChange = changeDelta.getChangedAttributes().get(docItemType.getId());
				
				if (attributeChange != null) {
					
					// document change i.e. file upload
					Map<String, Object> record = event.getDataAsMap();
					
					String docQId = docItemType.getQualifiedId();
					
					String docIdentity = ContentDataUtil.findDocIdentity(record, containerDef);
					String contentQId = containerDef.getQualifiedId();
					String itemTitle = (String) record.get(ContentDataConstants.CONTENT_TITLE_KEY);
					String itemIdentity = ContentDataUtil.getRecordIdentity(record);
					
					Update update = new Update().set("activity.docQId", docQId)
							.set("activity.containerQId", contentQId)
							.set("activity.itemTitle", itemTitle)
							.set("activity.itemIdentity", itemIdentity);
					
					Query query = Query.query(Criteria.where("activity.docIdentity").is(docIdentity)
														.and("activity.activityType").is("DOC_UPLOAD"));
					
					dao.updateMulti(query, update, ActivityAudit.class);
				}
			}
			
		}
		
	}

	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://test.enablix.com/login.html");
		System.out.println(url.getHost());
	}
	
}
