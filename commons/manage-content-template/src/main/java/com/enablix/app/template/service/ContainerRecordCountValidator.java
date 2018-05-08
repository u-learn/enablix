package com.enablix.app.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.ErrorCodes;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.commons.validate.Validator;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class ContainerRecordCountValidator implements Validator<ContainerType> {

	@Autowired
	private ContentCrudService crud;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public AppError validate(ContainerType input) {
		
		if (input.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_CONTENT) {
		
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			String collectionName = template.getCollectionName(input.getQualifiedId());
			
			if (collectionName != null) {
			
				Long count = crud.getRecordCount(collectionName);
				
				if (count > 0) {
					
					String msg = "There are '" + count + "' " + input.getLabel() + " in the system. "
							+ "<br><br>Please delete all the " + input.getSingularLabel() + " assets before deleting this Content Type.";
					return new AppError(ErrorCodes.CONTAINER_REC_EXIST, msg);
				}
			}
		}
		
		return null;
	}

}
