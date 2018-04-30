package com.enablix.core.mail;

public interface GenericEmailVelocityInputBuilderFactory {

	GenericEmailVelocityInputBuilder<?> getBuilder(String mailType); 
	
}
