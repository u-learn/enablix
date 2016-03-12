package com.enablix.core.mongo.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.AESParameterProvider;
import com.enablix.commons.util.DefaultAESParameterProvider;
import com.enablix.commons.util.EncryptionUtil;
import com.enablix.core.domain.config.EmailConfiguration;
import com.mongodb.DBObject;

@Component
public class EmailPasswordEncryptor extends AbstractMongoEventListener<EmailConfiguration> {

	private static final String PASSWORD_FIELD = "password";
	
	private AESParameterProvider aesParamProvider = new DefaultAESParameterProvider();
	
	@Override
	public void onAfterLoad(DBObject emailDbo) {

		String encPassword = getPassword(emailDbo);
		
		if (encPassword != null) {
			String password = EncryptionUtil.getAesDecryptedString(encPassword, aesParamProvider); 
			emailDbo.put(PASSWORD_FIELD, password);
		}
	}
	
	public void onBeforeSave(EmailConfiguration source, DBObject emailDbo) {
		
		String password = getPassword(emailDbo);
		
		if (password != null) {
			String encPassword = EncryptionUtil.getAesEncryptedString(password, aesParamProvider);
			emailDbo.put(PASSWORD_FIELD, encPassword);
		}
		
	}
	
	private String getPassword(DBObject userDbo) {
		Object password = userDbo.get(PASSWORD_FIELD);
		return password != null ? String.valueOf(password) : null;
	}
	
}
