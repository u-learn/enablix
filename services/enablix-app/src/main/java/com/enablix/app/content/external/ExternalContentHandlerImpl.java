package com.enablix.app.content.external;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.mapper.ContentMapper;
import com.enablix.content.mapper.ContentMapperRegistry;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.core.api.TemplateFacade;

@Component
public class ExternalContentHandlerImpl implements ExternalContentHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalContentHandlerImpl.class);
	
	@Autowired
	private ContentMapperRegistry mapperRegistry;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Override
	public String storeContent(ExternalContent content) {
		
		ContentMapper mapper = mapperRegistry.getMapper(content);
		
		if (mapper == null) {
			LOGGER.error("No mapper found for [{}], content [{}]", content.getContentSource(), content.getContentQId());
			throw new UnsupportedOperationException("No mapper found");
		}
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		EnablixContent ebxContent = mapper.transformToEnablixContent(content, template);
		
		UpdateContentRequest request = new UpdateContentRequest(template.getId(), 
				ebxContent.getParentIdentity(), content.getContentQId(), ebxContent.getData());
		request.setQualityAlertProcessing(QualityAlertProcessing.CONTINUE);
		
		UpdateContentResponse response = dataMgr.saveData(request);
		Map<String, Object> savedData = response.getContentRecord();
		
		return (String) savedData.get(ContentDataConstants.IDENTITY_KEY);
	}

}
