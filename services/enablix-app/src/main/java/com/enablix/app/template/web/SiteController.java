package com.enablix.app.template.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteController {

	@RequestMapping(method = RequestMethod.GET, value="/terms")
	public void termsAndConditions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response, "/site-doc/TermsConditions.pdf");
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String forwardUrl)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(forwardUrl);
		rd.forward(request, response);
	}

	@RequestMapping(method = RequestMethod.GET, value="/privacy")
	public void privacyPolicy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response, "/site-doc/PrivacyPolicy.pdf");
	}

}
