package com.enablix.site.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.security.GoogleNoCaptchValidator;
import com.enablix.core.security.GoogleNoCaptchaProperties;
import com.enablix.site.contactus.ContactUsManager;
import com.enablix.website.ContactUsRequest;

@RestController
public class SiteController {
	
	@Autowired
	private ContactUsManager contactUsMgr;
	
	@Autowired
	private GoogleNoCaptchValidator captchaValidator;
	
	@Autowired
	private GoogleNoCaptchaProperties captchaProp;
	
	@RequestMapping(method = RequestMethod.GET, value="/terms")
	public void termsAndConditions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response, "/site-doc/TermsConditions.pdf");
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/site/captchasitekey", produces="application/json")
	public CaptchaSiteKeyWrapper captchaSiteKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return new CaptchaSiteKeyWrapper(captchaProp.getClientSiteKey());
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

	@RequestMapping(method = RequestMethod.POST, value="/site/contactus")
	public void contactUs(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody ContactUsForm contactUs) throws ServletException, IOException {
		
		if (!captchaValidator.validateCaptchaResponse(contactUs.getCaptchaResponse(), request.getRemoteHost())) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CAPTCHA_ERROR");
			return;
		}
		
		contactUsMgr.captureContactUsRequest(contactUs.getContactUs());
	}
	
	public static class ContactUsForm {
		
		private String captchaResponse;
		private ContactUsRequest contactUs;
		
		public String getCaptchaResponse() {
			return captchaResponse;
		}
		public void setCaptchaResponse(String captchaResponse) {
			this.captchaResponse = captchaResponse;
		}
		public ContactUsRequest getContactUs() {
			return contactUs;
		}
		public void setContactUs(ContactUsRequest contactUs) {
			this.contactUs = contactUs;
		}
		
	}

	static class CaptchaSiteKeyWrapper {
		private String sitekey;
		public CaptchaSiteKeyWrapper(String sitekey) {
			this.sitekey = sitekey;
		}
		public String getSitekey() {
			return sitekey;
		}
		public void setSitekey(String sitekey) {
			this.sitekey = sitekey;
		}
	}
}
