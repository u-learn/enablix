package com.enablix.app.content.data.segment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentRecord;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.data.segment.view.DataSegmentConnector;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;
import com.enablix.services.util.ContentDataUtil;

public abstract class ContentRecordRefDataSegmentConnector<T extends DataSegmentAware> implements DataSegmentConnector<T> {

	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;

	public ContentRecordRefDataSegmentConnector() {
		super();
	}

	@Override
	public DataSegmentInfo buildDataSegmentInfo(T content) {
		return dsInfoBuilder.build(getContentRecordRef(content));
	}
	
	protected abstract ContentRecordRef getContentRecordRef(T data);

	@Override
	public void updateAllRecords(ContentTemplate template, DataSegmentDefinitionType dataSegmentDef) {
	
		Pageable pageable = new PageRequest(0, 10);
		
		Page<T> page = null;
				
		do {
			
			page = getNextPageToProcess(pageable);
			
			if (page != null) {
				
 				for (T data : page) {
					DataSegmentInfo dsSegmentInfo = buildDataSegmentInfo(data);
					data.setDataSegmentInfo(dsSegmentInfo);
				}
				
 				saveUpdatedPage(page);
				pageable = pageable.next();
			}
			
		} while (page != null && pageable.getPageNumber() < page.getTotalPages() && page.getSize() > 0);
	
	}
	
	protected abstract Page<T> getNextPageToProcess(Pageable pageable);
	
	protected abstract void saveUpdatedPage(Iterable<T> page);

	@Override
	public void updateRecord(ContentRecord updatedContent, DataSegmentInfo dataSegmentInfo) {
		
		List<T> records = getRecords(updatedContent);
		
		if (CollectionUtil.isNotEmpty(records)) {
		
			for (T data : records) {
				data.setDataSegmentInfo(dataSegmentInfo);
			}
			
			saveUpdatedPage(records);
		}
		
	}
	
	protected List<T> getRecords(ContentRecord updatedContent) {
		String recordIdentity = ContentDataUtil.getRecordIdentity(updatedContent.getRecord());
		return getRecordsForIdentity(recordIdentity);
	}
	
	protected abstract List<T> getRecordsForIdentity(String contentIdentity);

}