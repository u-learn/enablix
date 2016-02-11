package com.enablix.core.mongo.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.AESParameterProvider;
import com.enablix.commons.util.DefaultAESParameterProvider;
import com.enablix.commons.util.EncryptionUtil;
import com.enablix.core.domain.user.User;
import com.mongodb.DBObject;

@Component
public class UserPasswordEncryptor extends AbstractMongoEventListener<User> {

	private static final String PASSWORD_FIELD = "password";
	
	private AESParameterProvider aesParamProvider = new DefaultAESParameterProvider();
	
	@Override
	public void onAfterLoad(DBObject userDbo) {

		String encPassword = getPassword(userDbo);
		
		if (encPassword != null) {
			String password = EncryptionUtil.getAesDecryptedString(encPassword, aesParamProvider); 
			userDbo.put(PASSWORD_FIELD, password);
		}
	}
	
	public void onBeforeSave(User source, DBObject userDbo) {
		
		String password = getPassword(userDbo);
		
		if (password != null) {
			String encPassword = EncryptionUtil.getAesEncryptedString(password, aesParamProvider);
			userDbo.put(PASSWORD_FIELD, encPassword);
		}
		
	}
	
	private String getPassword(DBObject userDbo) {
		Object password = userDbo.get(PASSWORD_FIELD);
		return password != null ? String.valueOf(password) : null;
	}
	
}
