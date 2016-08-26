package com.enablix.app.content.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.share.ShareContentConstants;
import com.enablix.app.template.web.TemplateController;
import com.enablix.core.domain.share.SharedSiteUrl;
import com.enablix.core.system.repo.SharedSiteUrlRepository;

@RestController
public class SharedContentAccessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private SharedSiteUrlRepository sharedUrlRepo;
	
	@RequestMapping(method = RequestMethod.GET, value = ShareContentConstants.UNSECURE_SHARE_URL_PREFIX + "/{sharedUrl}")
	public void unsecureSharedUrl(@PathVariable String sharedUrl, 
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOGGER.debug("Unsecure shared url: {}", sharedUrl);
		
		SharedSiteUrl sharedSiteUrl = sharedUrlRepo.findBySharedUrl(
				ShareContentConstants.UNSECURE_SHARE_URL_PREFIX + "/" + sharedUrl);
		
		if (sharedSiteUrl != null) {
			forward(request, response, sharedSiteUrl.getActualUrl());
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, sharedUrl + " not found");
		}
		
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response, String forwardUrl)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(forwardUrl);
		rd.forward(request, response);
	}
	
}