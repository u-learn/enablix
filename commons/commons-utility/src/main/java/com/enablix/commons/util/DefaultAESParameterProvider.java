package com.enablix.commons.util;

public class DefaultAESParameterProvider implements AESParameterProvider {

	// TODO: need to setup tenant specific security key
	private static final String TEMP_SECURITY_KEY = "f4844b1923eb4d50883ecc807c287640";//UUID.randomUUID().toString().replaceAll("-", "");

	@Override
	public String ivParameter() {
		return TEMP_SECURITY_KEY;
	}
	
	@Override
	public String passphrase() {
		return TEMP_SECURITY_KEY + TEMP_SECURITY_KEY;
	}
	
	@Override
	public String salt() {
		return TEMP_SECURITY_KEY + TEMP_SECURITY_KEY + TEMP_SECURITY_KEY;
	}
	
}
