package com.enablix.core.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.enablix.core.domain.security.RememberMeToken;
import com.enablix.core.security.token.repo.RememberMeTokenRepository;

public class MongoPersistentTokenRepository implements PersistentTokenRepository {

	@Autowired
	private RememberMeTokenRepository tokenRepo;
	
	@Autowired
	private RememberMeTokenArchive tokenArchive;
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		
		PersistentRememberMeToken current = getTokenForSeries(token.getSeries());

		if (current != null) {
			throw new DataIntegrityViolationException("Series Id '" + token.getSeries()
					+ "' already exists!");
		}

		RememberMeToken rememberMeToken = new RememberMeToken();
		rememberMeToken.setToken(token);
		
		tokenRepo.save(rememberMeToken);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		
		RememberMeToken rememberMeToken = tokenRepo.findByTokenSeries(series);

		// archive to tackle simultaneous calls
		PersistentRememberMeToken existToken = rememberMeToken.getToken();
		tokenArchive.putToken(existToken);

		PersistentRememberMeToken newToken = new PersistentRememberMeToken(
				existToken.getUsername(), series, tokenValue, lastUsed);

		rememberMeToken.setToken(newToken);
		
		tokenRepo.save(rememberMeToken);
		
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		RememberMeToken rememberMeToken = tokenRepo.findByTokenSeries(seriesId);
		return rememberMeToken != null ? rememberMeToken.getToken() : null;
	}

	@Override
	public void removeUserTokens(String username) {
		tokenRepo.deleteByTokenUsername(username);
	}
	
}
