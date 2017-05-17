package com.enablix.doc.preview.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.repository.DocPreviewDataRepository;
import com.enablix.commons.util.beans.BeanUtil;

@Component
public class DocPreviewDataCrudService extends MongoRepoCrudService<DocPreviewData> {

	@Autowired
	private DocPreviewDataRepository repo;
	
	@Override
	public DocPreviewDataRepository getRepository() {
		return repo;
	}

	@Override
	public DocPreviewData findExisting(DocPreviewData t) {
		return repo.findByDocIdentity(t.getDocIdentity());
	}
	
	@Override
	protected DocPreviewData merge(DocPreviewData t, DocPreviewData existing) {
		BeanUtil.copyBusinessAttributes(t, existing);
		return existing;
	}

}
