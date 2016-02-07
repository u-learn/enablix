package com.enablix.commons.util;

public interface AESParameterProvider {

	String salt();
	
	String passphrase();
	
	String ivParameter();
	
}
