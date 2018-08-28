package com.enablix.site.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.security.GoogleNoCaptchValidator;
import com.enablix.core.security.GoogleNoCaptchaProperties;
import com.enablix.core.security.SecurityUtil;
import com.enablix.core.security.service.EnablixUserService;
import com.enablix.site.contactus.ContactUsManager;
import com.enablix.website.ContactUsRequest;

import canvas.SignedRequest;

@RestController
public class SiteController {
	
	@Autowired
	private ContactUsManager contactUsMgr;
	
	@Autowired
	private GoogleNoCaptchValidator captchaValidator;
	
	@Autowired
	private GoogleNoCaptchaProperties captchaProp;
	
	@Autowired
	private EnablixUserService userService;
	
	@RequestMapping(method = RequestMethod.GET, value="/terms")
	public void termsAndConditions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response, "/site-doc/TermsConditions.pdf");
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/site/captchasitekey", produces="application/json")
	public CaptchaSiteKeyWrapper captchaSiteKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return new CaptchaSiteKeyWrapper(captchaProp.getClientSiteKey());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/features")
	public void siteFeatures(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String htmlPage = "/site-features.html";
		forward(request, response, htmlPage);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/pricing")
	public void sitePricing(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String htmlPage = "/site-pricing.html";
		forward(request, response, htmlPage);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/solution-sales-portal")
	public void siteSalesPortal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String htmlPage = "/site-solution-sales-portal.html";
		forward(request, response, htmlPage);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/solution-knowledge-portal")
	public void siteKnowledgePortal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String htmlPage = "/site-solution-knowledge-portal.html";
		forward(request, response, htmlPage);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/sales-enablement-hubspot")
	public void siteHubspotSales(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String htmlPage = "/site-sales-enablement-hubspot.html";
		forward(request, response, htmlPage);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/sales-enablement-startup")
	public void siteStartupSales(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String htmlPage = "/site-sales-enablement-startup.html";
		forward(request, response, htmlPage);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/sf/canvas/{app}")
	public void salesforceCanvasApp(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String app) throws ServletException, IOException {
		
		// Pull the signed request out of the request body and verify and decode it.
	    Map<String, String[]> parameters = request.getParameterMap();
	    String[] signedRequest = parameters.get("signed_request");
	    String yourConsumerSecret = "3687049815195892632";
	    String signedRequestJson = SignedRequest.verifyAndDecodeAsJson(signedRequest[0], yourConsumerSecret);
		System.out.println(signedRequestJson);
		
		UserDetails currentUser = SecurityUtil.currentUser();
		if (currentUser == null) {
			String userEmail = (String) JsonUtil.getJsonpathValue(signedRequestJson, "context.user.email");
			UserDetails userDetails = userService.loadUserByUsername(userEmail);
			if (userDetails != null) {
				SecurityUtil.loginUser(userDetails);
			}
		}
		
		String appPage = (String) JsonUtil.getJsonpathValue(signedRequestJson, "context.environment.parameters.appPage");
		if (!StringUtil.hasText(appPage)) {
			appPage = "/portal";
		}
		
		StringBuilder htmlPage = new StringBuilder();
		htmlPage.append(appPage).append("?ctx_embedded=true");
		
		Object appCtxObj = JsonUtil.getJsonpathValue(signedRequestJson, "context.environment.parameters.appCtx");
	    
		if (appCtxObj instanceof Map) {
	    
			Map<String, Object> appCtx = (Map<String, Object>) appCtxObj;
	    	
	    	for (Map.Entry<String, Object> entry : appCtx.entrySet()) {
	    		
    			htmlPage.append("&")
	    				.append("ctx_").append(entry.getKey())
	    				.append("=")
	    				.append(String.valueOf(entry.getValue()));
	    	}
	    }

	    response.sendRedirect(htmlPage.toString());
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String forwardUrl)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(forwardUrl);
		rd.forward(request, response);
	}

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD}, value="/privacy")
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
	
	@RequestMapping(method = RequestMethod.POST, value="/site/contactusnc")
	public void contactUsWithoutCaptcha(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody ContactUsRequest contactUs) throws ServletException, IOException {
		contactUsMgr.captureContactUsRequest(contactUs);
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
