package com.enablix.app.content.pack.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.pack.ContentPackDataResolver;
import com.enablix.app.content.pack.ContentPackManager;
import com.enablix.app.content.pack.repo.ContentPointerRepository;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.SelectedContentPack;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.core.domain.content.pack.ContentPack.Type;
import com.enablix.core.domain.content.pack.ContentPointer;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.mongo.MongoUtil;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@Component
public class SelectedContentPackManager implements ContentPackDataResolver<ContentPack> {

	@Autowired
	private ContentPointerRepository contentPointerRepo;
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentPackManager packManager;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	

	public ContentPack saveOrUpdate(SelectedContentPack request, DataView dataView) {
		
		ContentPack updatedPack = packManager.saveOrUpdate(request.getPack());

		contentPointerRepo.deleteByParentIdentity(updatedPack.getIdentity());
		
		List<ContentPointer> contentPointers = request.getRecords();
		
		for (ContentPointer contentPointer: contentPointers) {
			
			contentPointer.setParentIdentity(updatedPack.getIdentity());
			contentPointer.setParentType(ContentPointer.PARENT_TYPE_CONTENT_PACK);
			
			DataSegmentInfo dsInfo = dsInfoBuilder.build(contentPointer.getData());
			contentPointer.setDataSegmentInfo(dsInfo);
		}
		
		contentPointerRepo.save(contentPointers);
		return updatedPack;
	}
	
	@Override
	public Page<ContentDataRecord> getData(ContentPack pack, DataView dataView, int pageNo, int pageSize) {
		
		MongoDataView mongoDataView = DataViewUtil.getMongoDataView(dataView);
		PageRequest pageable = new PageRequest(pageNo, pageSize, Direction.ASC, AppConstants.ORDER_ATTR_ID);
		
		Page<ContentDataRecord> dataPage = null;
		
		Page<ContentPointer> pageContent = MongoUtil.executeWithDataViewScope(
				mongoDataView, () -> contentPointerRepo.findByParentIdentity(pack.getIdentity(), pageable));
		
		if (pageContent != null && pageContent.hasContent()) {
		
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			List<ContentDataRecord> dataRecords = new ArrayList<>();
			
			for (ContentPointer content : pageContent) {
				Map<String, Object> contentRecord = dataMgr.getContentRecord(content.getData(), template, dataView);
				
				if (CollectionUtil.isNotEmpty(contentRecord)) {
					dataRecords.add(new ContentDataRecord(
						template.getId(), content.getData().getContainerQId(), contentRecord));
				}
			}
			
			dataPage = new PageImpl<>(dataRecords, pageable, pageContent.getTotalElements());
		}
		
		return dataPage;
	}

	@Override
	public Type resolverPackType() {
		return ContentPack.Type.SELECTED_CONTENT;
	}

}
