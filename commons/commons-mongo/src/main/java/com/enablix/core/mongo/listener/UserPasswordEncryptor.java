package com.enablix.core.mongo.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.EncryptionUtil;
import com.enablix.core.domain.user.User;
import com.mongodb.DBObject;

@Component
public class UserPasswordEncryptor extends AbstractMongoEventListener<User> {

	private static final String PASSWORD_FIELD = "password";
	
	// TODO: need to setup tenant specific security key
	private static final String TEMP_SECURITY_KEY = "f4844b1923eb4d50883ecc807c287640";//UUID.randomUUID().toString().replaceAll("-", "");
	
	@Override
	public void onAfterLoad(DBObject userDbo) {

		String encPassword = getPassword(userDbo);
		
		if (encPassword != null) {
			String password = EncryptionUtil.getAesDecryptedString(encPassword, 
				getPassphrase(), getIvParameter(), getSalt());
			userDbo.put(PASSWORD_FIELD, password);
		}
	}
	
	public void onBeforeSave(User source, DBObject userDbo) {
		
		String password = getPassword(userDbo);
		
		if (password != null) {
			String encPassword = EncryptionUtil.getAesEncryptedString(password, 
				getPassphrase(), getIvParameter(), getSalt());
			userDbo.put(PASSWORD_FIELD, encPassword);
		}
		
	}
	
	private String getPassword(DBObject userDbo) {
		Object password = userDbo.get(PASSWORD_FIELD);
		return password != null ? String.valueOf(password) : null;
	}
	
	private String getIvParameter() {
		return TEMP_SECURITY_KEY;
	}
	
	private String getPassphrase() {
		return TEMP_SECURITY_KEY + TEMP_SECURITY_KEY;
	}
	
	private String getSalt() {
		return TEMP_SECURITY_KEY + TEMP_SECURITY_KEY + TEMP_SECURITY_KEY;
	}

	public static void main(String[] args) {
		
		UserPasswordEncryptor enc = new UserPasswordEncryptor();
		System.out.println(TEMP_SECURITY_KEY);
		System.out.println(EncryptionUtil.getAesEncryptedString("password", 
				enc.getPassphrase(), enc.getIvParameter(), enc.getSalt()));
	}
}
