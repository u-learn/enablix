package com.enablix.commons.util.id;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDIdentityGenerator implements IdentityGenerator {

	@Override
	public String generateId(Object forObject) {
		return UUID.randomUUID().toString();
	}

}
