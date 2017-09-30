package com.enablix.core.mongo.listener;

import java.util.Map;
import java.util.function.Function;

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
	
	private static final String DATA_PROP = "data";
	
	private AESParameterProvider aesParamProvider = new DefaultAESParameterProvider();
	
	@Override
	public void onAfterLoad(DBObject userDbo) {

		checkAndProcessEncProperties(userDbo, 
				(propVal) -> EncryptionUtil.getAesDecryptedString(propVal, aesParamProvider));
		
	}
	
	public void onBeforeSave(Configuration source, DBObject userDbo) {
		
		checkAndProcessEncProperties(userDbo, 
				(propVal) -> EncryptionUtil.getAesEncryptedString(propVal, aesParamProvider));
				
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkAndProcessEncProperties(DBObject userDbo, Function<String, String> propProcessor) {
		
		Map<String, Object> configProperties = getConfigProperties(userDbo);
		
		if (configProperties != null) {
			
			for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
				
				if (entry.getKey().endsWith(ENC_PROP_SUFFIX)) {
					
					Object val = entry.getValue();
					
					if (val instanceof String) {
				
						String strValue = (String) entry.getValue();
						
						if (!StringUtil.isEmpty(strValue)) {
							String processedVal = propProcessor.apply(strValue);
							configProperties.put(entry.getKey(), processedVal);
						}
						
					} else if (val instanceof Map) {
						
						Map mapVal = (Map) val;
						
						Object data = mapVal.get(DATA_PROP);
						if (data instanceof String && !StringUtil.isEmpty((String) data)) {
							String processedVal = propProcessor.apply((String) data);
							mapVal.put(DATA_PROP, processedVal);
						}
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
