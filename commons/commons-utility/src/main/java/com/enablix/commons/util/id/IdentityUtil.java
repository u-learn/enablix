package com.enablix.commons.util.id;

public class IdentityUtil {

	private static IdentityGenerator IDENTITY_GENERATOR;
	
	public static void registerIdentityGenerator(IdentityGenerator idGenerator) {
		IDENTITY_GENERATOR = idGenerator;
	}
	
	public static String generateIdentity(Object forObj) {
		return IDENTITY_GENERATOR.generateId(forObj);
	}
	
}
