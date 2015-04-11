package com.enablix.commons.util.id;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDIdentityGenerator implements IdentityGenerator {

	public UUIDIdentityGenerator() {
		IdentityUtil.registerIdentityGenerator(this);
	}
	
	@Override
	public String generateId(Object forObject) {
		return UUID.randomUUID().toString();
	}

}
