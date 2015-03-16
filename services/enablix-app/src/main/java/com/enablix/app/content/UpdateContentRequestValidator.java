package com.enablix.app.content;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.validate.Validator;

@Component
public class UpdateContentRequestValidator implements Validator<UpdateContentRequest> {

	/*
	 * Validations:
	 * 1. If new record, then containerQId should be of the root element
	 */
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public Collection<Error> validate(UpdateContentRequest t) {
		// TODO Auto-generated method stub
		return null;
	}

}
