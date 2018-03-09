package com.enablix.doc.preview.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentStoreFactory;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.dms.repository.DocPreviewDataRepository;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.DocInfo;
import com.enablix.doc.preview.ThumbnailSize;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class PreviewThumbnailGenTask implements Task {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviewThumbnailGenTask.class);

	@Autowired
	private DocPreviewDataRepository previewDataRepo;
	
	@Autowired
	private ImagePreviewGenerator imgPreviewGen;
	
	@Autowired
	private DocumentStoreFactory docStoreFactory;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void run(TaskContext context) {
		
		Pageable pageable = new PageRequest(0, 10);
		
		String storeType = docStoreFactory.getStoreType(DocumentStoreConstants.PREVIEW_DOC_STORE_CONFIG_PROP);
		DocumentStore<?,?> previewDocStore = docStoreFactory.getDocumentStore(storeType);
		
		String stWidthProp = "smallThumbnail.properties.width";
		
		List<String> failedIds = new ArrayList<String>();
		
		Criteria criteria = Criteria.where("identity").nin(failedIds)
								.orOperator(Criteria.where(stWidthProp).exists(false), 
										Criteria.where(stWidthProp).gt(ThumbnailSize.SMALL.getMaxWidth()));
		
		Query query = Query.query(criteria);
		
		while (true) {
			
			query.with(pageable);
			
			List<DocPreviewData> records = mongoTemplate.find(query, DocPreviewData.class);
			
			if (CollectionUtil.isNotEmpty(records)) {
		
				for (DocPreviewData previewData : records) {
					
					List<DocInfo> parts = previewData.getParts();
					
					if (CollectionUtil.isNotEmpty(parts)) {
					
						try {
							
							imgPreviewGen.generateThumbnails(previewDocStore, previewData, parts.get(0));
							previewDataRepo.save(previewData);
							
						} catch (IOException e) {
							LOGGER.error("Error generating thumbnails for [" + previewData.getDocIdentity() + "]", e);
							failedIds.add(previewData.getIdentity());
						}
					}
				}
				
			} else {
				break;
			}

		}
		
	}

	@Override
	public String taskId() {
		return "preview-thumbnail-gen-task";
	}

}
