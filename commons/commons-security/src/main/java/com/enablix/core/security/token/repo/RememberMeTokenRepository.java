package com.enablix.core.security.token.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.enablix.core.domain.security.RememberMeToken;

public interface RememberMeTokenRepository extends MongoRepository<RememberMeToken, String> {

	RememberMeToken findByTokenSeries(String series);
	
	void deleteByTokenUsername(String username);
	
}
