package com.enablix.data.segment.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentRecord;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;

@Component
public class DataSegmentInfoBuilderImpl implements DataSegmentInfoBuilder {

	@Autowired
	private ContentCrudService contentCrud;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public DataSegmentInfo build(ContentRecord record) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateManager.getTemplateFacade(templateId);
		
		return buildDataSegmentInfo(record.getRecord(), record.getContentQId(), template);
	}

	private DataSegmentInfo buildDataSegmentInfo(
			Map<String, Object> record, String contentQId, TemplateFacade template) {
		
		DataSegmentInfo dsInfo = null;
		
		if (record != null) {
			
			dsInfo = new DataSegmentInfo();
			
			for (String dsAttrId : template.getDataSegmentAttrIds()) {
				
				ContentItemType contentItem = 
						template.getContainerContentItemForDataSegmentAttr(contentQId, dsAttrId);
				
				if (contentItem != null) {
					dsInfo.addAttribute(dsAttrId, record.get(contentItem.getId()));
				}
			}
		}
		
		return dsInfo;
	}

	@Override
	public DataSegmentInfo build(ContentRecordRef dataRef) {
		return build(dataRef.getContentQId(), dataRef.getRecordIdentity());
	}

	@Override
	public DataSegmentInfo build(String contentQId, String recordIdentity) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateManager.getTemplateFacade(templateId);
		
		DataSegmentInfo dsInfo = null;
		
		String collectionName = template.getCollectionName(contentQId);
		
		if (collectionName != null) {
			Map<String, Object> record = contentCrud.findRecord(
				collectionName, recordIdentity, MongoDataView.ALL_DATA);
		
			dsInfo = buildDataSegmentInfo(record, contentQId, template);
		}
		
		return dsInfo;
	}

}
