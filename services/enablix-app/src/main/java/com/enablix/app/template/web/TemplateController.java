package com.enablix.app.template.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("template")
public class TemplateController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
	
	@RequestMapping(method = RequestMethod.POST, value="/upload", consumes = "application/xml")
	public String importTemplate(HttpServletRequest request) {
		LOGGER.debug("Importing content template");
		return "success";
	}
	
}
