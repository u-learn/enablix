package com.enablix.app.mail.generic;

public interface GenericEmailVelocityInputBuilderFactory {

	GenericEmailVelocityInputBuilder<?> getBuilder(String mailType); 
	
}
