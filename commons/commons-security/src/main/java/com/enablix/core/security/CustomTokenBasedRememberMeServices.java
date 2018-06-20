package com.enablix.core.security;

import java.lang.reflect.Method;
import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.ReflectionUtils;

import com.enablix.commons.util.StringUtil;

public class CustomTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

	@Autowired
	private LoginListener loginListener;
	
	@Autowired
	private LastRememberMeLoginCache lastLoginCache;
	
	@Value("${server.session.cookie.domain}")
	private String cookieDomain;
	
	private Method setHttpOnlyMethod;
	
	public CustomTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
		this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
	}

	@Override
	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		
		if (!super.rememberMeRequested(request, parameter)) {
			
			String headerValue = request.getHeader(parameter);

			if (headerValue != null) {
				if (headerValue.equalsIgnoreCase("true") || headerValue.equalsIgnoreCase("on")
						|| headerValue.equalsIgnoreCase("yes") || headerValue.equals("1")) {
					return true;
				}
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("Did not send remember-me cookie (principal did not set header '"
						+ parameter + "')");
			}
			
		} else {
			return true;
		}

		return false;
	}
	
	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response) {
		
		UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
		
		if (shouldAuditLogin(userDetails.getUsername())) {
			// audit user login via remember-me token only once a day
			loginListener.auditUserLogin(userDetails);
		}
		
		return userDetails;
	}
	
	private boolean shouldAuditLogin(String username) {
		
		Calendar currDate = Calendar.getInstance();
		int presentDayOfYear = currDate.get(Calendar.DAY_OF_YEAR);
		
		int lastLoginDayOfYear = lastLoginCache.getAndSetLastRememberMeLoginDay(username, presentDayOfYear);

		return lastLoginDayOfYear != presentDayOfYear;
	}
	
	protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request,
			HttpServletResponse response) {
		String cookieValue = encodeCookie(tokens);
		Cookie cookie = new Cookie(getCookieName(), cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath(getCookiePath(request));

		// CUSTOMIZATION: adding cookie domain to remember-me cookie
		if (StringUtil.hasText(cookieDomain)) {
			cookie.setDomain(cookieDomain);
		}
		
		if (maxAge < 1) {
			cookie.setVersion(1);
		}

		cookie.setSecure(request.isSecure());

		if (setHttpOnlyMethod != null) {
			ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
		}
		else if (logger.isDebugEnabled()) {
			logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
		}

		response.addCookie(cookie);
	}
	
	protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Cancelling cookie");
		Cookie cookie = new Cookie(getCookieName(), null);
		cookie.setMaxAge(0);
		cookie.setPath(getCookiePath(request));
		
		// CUSTOMIZATION: adding cookie domain to remember-me cookie
		if (StringUtil.hasText(cookieDomain)) {
			cookie.setDomain(cookieDomain);
		}

		response.addCookie(cookie);
	}
	
	private String getCookiePath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return contextPath.length() > 0 ? contextPath : "/";
	}
	
}
