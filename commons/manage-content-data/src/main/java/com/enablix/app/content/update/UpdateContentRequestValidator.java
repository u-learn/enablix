package com.enablix.app.content.update;

import org.springframework.stereotype.Component;

import com.enablix.commons.exception.AppError;
import com.enablix.commons.validate.Validator;

@Component
public class UpdateContentRequestValidator implements Validator<UpdateContentRequest> {

	/*
	 * Validations:
	 * 1. If new record, then containerQId should be of the root element
	 */
	
	//@Autowired
	//private TemplateManager templateMgr;
	
	@Override
	public AppError validate(UpdateContentRequest t) {
		// TODO Auto-generated method stub
		return null;
	}

}
