package com.enablix.doc.preview.crud;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.doc.preview.DocPreviewService;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class PreviewGeneratorTask implements Task {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviewGeneratorTask.class);

	@Autowired
	private DocumentMetadataRepository docMetadataRepo;
	
	@Autowired
	private DocPreviewService previewService;
	
	@Autowired
	private DocumentManager docManager;
	
	@Override
	public void run(TaskContext context) {
		
		Collection<DocumentMetadata> previewGenCandidates = 
				docMetadataRepo.findByPreviewStatusExistsOrPreviewStatus(Boolean.FALSE, PreviewStatus.PENDING);
		
		for (DocumentMetadata docMd : previewGenCandidates) {
			
			try {
				
				if (docManager.checkReferenceRecordExists(docMd)) {
					previewService.createPreview(docMd);
				}
				
			} catch (Throwable e) {
				LOGGER.error("Error generating preview for document [" + docMd.getIdentity() + "]", e);
				docManager.updatePreviewStatus(docMd.getIdentity(), PreviewStatus.FAILED);
			}
		}
		
	}

	@Override
	public String taskId() {
		return "preview-generator-task";
	}

}
