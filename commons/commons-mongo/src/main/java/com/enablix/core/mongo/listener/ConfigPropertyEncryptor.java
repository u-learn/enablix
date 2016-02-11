package com.enablix.core.mongo.listener;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.AESParameterProvider;
import com.enablix.commons.util.DefaultAESParameterProvider;
import com.enablix.commons.util.EncryptionUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.config.Configuration;
import com.mongodb.DBObject;

@Component
public class ConfigPropertyEncryptor extends AbstractMongoEventListener<Configuration> {

	private static final String ENC_PROP_SUFFIX = "_ENC";
	
	private static final String CONFIG_FIELD_NAME = "config";
	
	private AESParameterProvider aesParamProvider = new DefaultAESParameterProvider();
	
	@Override
	public void onAfterLoad(DBObject userDbo) {

		Map<String, Object> configProperties = getConfigProperties(userDbo);
		
		if (configProperties != null) {
			
			for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
			
				if (entry.getKey().endsWith(ENC_PROP_SUFFIX) && entry.getValue() instanceof String) {
				
					String strValue = (String) entry.getValue();
					
					if (!StringUtil.isEmpty(strValue)) {
						String decValue = EncryptionUtil.getAesDecryptedString(strValue, aesParamProvider);
						configProperties.put(entry.getKey(), decValue);
					}
				}
			}
		}
	}
	
	public void onBeforeSave(Configuration source, DBObject userDbo) {
		
		Map<String, Object> configProperties = getConfigProperties(userDbo);
		
		if (configProperties != null) {
			
			for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
				
				if (entry.getKey().endsWith(ENC_PROP_SUFFIX) && entry.getValue() instanceof String) {
				
					String strValue = (String) entry.getValue();
					
					if (!StringUtil.isEmpty(strValue)) {
						String decValue = EncryptionUtil.getAesEncryptedString(strValue, aesParamProvider);
						configProperties.put(entry.getKey(), decValue);
					}
				}
			}
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> getConfigProperties(DBObject userDbo) {
		Object configProperties = userDbo.get(CONFIG_FIELD_NAME);
		return configProperties != null && configProperties instanceof Map? (Map) configProperties : null;
	}
	
}
