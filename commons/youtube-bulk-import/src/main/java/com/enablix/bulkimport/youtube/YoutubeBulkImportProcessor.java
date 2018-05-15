package com.enablix.bulkimport.youtube;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.bulkimport.ImportContext;
import com.enablix.app.content.bulkimport.ImportProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.content.ImportRecord;
import com.enablix.core.domain.content.ImportRequest;
import com.enablix.services.util.TemplateUtil;

@Component
public class YoutubeBulkImportProcessor implements ImportProcessor {

	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public String importSource() {
		return "YOUTUBE";
	}

	@Override
	public void validateRequest(ImportRequest request) throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ImportProcessedInfo processRecord(ImportRecord record, ContainerType container, ImportContext ctx) throws IOException {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		String lblAttrId = template.getPortalLabelAttributeId(container.getQualifiedId());
		String urlAttribute = TemplateUtil.getUrlAttribute(container, lblAttrId);
		
		ImportProcessedInfo info = null;
		
		if (urlAttribute != null) {
			
			Object videoUrl = record.getSourceRecord().get("url");
			
			if (videoUrl != null) {
				Map<String, Object> recAttrs = new HashMap<>();
				recAttrs.put(urlAttribute, videoUrl);
				
				info = new ImportProcessedInfo(null, recAttrs);
			}
		}
		
		return info;
	}

}
