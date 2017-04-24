package com.enablix.app.main;

import java.io.IOException;

import org.springframework.core.io.Resource;

public interface CustomResourcePathBuilder {

	Resource createCustomLocation(Resource location) throws IOException;
	
}
