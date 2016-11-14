package com.enablix.core.security;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

public class BasicAuthPersistedRememberMeServices extends PersistentTokenBasedRememberMeServices {
	
	private MongoPersistentTokenRepository tokenRepository;
	
	@Autowired
	private RememberMeTokenArchive tokenArchive;
	
	public BasicAuthPersistedRememberMeServices(String key, UserDetailsService userDetailsService,
			MongoPersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		this.tokenRepository = tokenRepository;
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
	
	/**
	 * Locates the presented cookie data in the token repository, using the series id. If
	 * the data compares successfully with that in the persistent store, a new token is
	 * generated and stored with the same series. The corresponding cookie value is set on
	 * the response.
	 *
	 * @param cookieTokens the series and token values
	 *
	 * @throws RememberMeAuthenticationException if there is no stored token corresponding
	 * to the submitted cookie, or if the token in the persistent store has expired.
	 * @throws InvalidCookieException if the cookie doesn't have two tokens as expected.
	 * @throws CookieTheftException if a presented series value is found, but the stored
	 * token is different from the one presented.
	 */
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response) {

		if (cookieTokens.length != 2) {
			throw new InvalidCookieException("Cookie token did not contain " + 2
					+ " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];

		PersistentRememberMeToken token = tokenRepository
				.getTokenForSeries(presentedSeries);

		if (token == null) {
			// No series match, so we can't authenticate using this cookie
			throw new RememberMeAuthenticationException(
					"No persistent token found for series id: " + presentedSeries);
		}

		// We have a match for this user/series combination
		if (!presentedToken.equals(token.getTokenValue())) {
			
			PersistentRememberMeToken archivedToken = tokenArchive.getToken(presentedToken);
			
			if (archivedToken == null) {
				// Token doesn't match series value. Delete all logins for this user and throw
				// an exception to warn them.
				tokenRepository.removeUserTokens(token.getUsername());
	
				throw new CookieTheftException(
						messages.getMessage(
								"PersistentTokenBasedRememberMeServices.cookieStolen",
								"Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
			} else {
				token = archivedToken;
			}
			
		}

		if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System
				.currentTimeMillis()) {
			throw new RememberMeAuthenticationException("Remember-me login has expired");
		}

		// Token also matches, so login is valid. Update the token value, keeping the
		// *same* series number.
		if (logger.isDebugEnabled()) {
			logger.debug("Refreshing persistent login token for user '"
					+ token.getUsername() + "', series '" + token.getSeries() + "'");
		}

		PersistentRememberMeToken newToken = new PersistentRememberMeToken(
				token.getUsername(), token.getSeries(), generateTokenData(), new Date());

		try {
			tokenRepository.updateToken(newToken.getSeries(), newToken.getTokenValue(),
					newToken.getDate());
			addCookie(newToken, request, response);
		}
		catch (Exception e) {
			logger.error("Failed to update token: ", e);
			throw new RememberMeAuthenticationException(
					"Autologin failed due to data access problem");
		}

		return getUserDetailsService().loadUserByUsername(token.getUsername());
	}

	private void addCookie(PersistentRememberMeToken token, HttpServletRequest request,
			HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() },
				getTokenValiditySeconds(), request, response);
	}
	
}
