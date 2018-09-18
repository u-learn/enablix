package com.enablix.ms.graph;

import org.slf4j.Logger;
import org.springframework.web.client.HttpStatusCodeException;

public class MSGraphUtil {

	public static void logAndThrowMSGraphException(Logger LOGGER, String msg, Exception e) throws MSGraphException {
		logException(LOGGER, msg, e);
		throw new MSGraphException(msg, e);
	}

	public static void logException(Logger LOGGER, String msg, Exception e) {

		LOGGER.error(msg, e);
		
		if (e instanceof HttpStatusCodeException) {
			HttpStatusCodeException ce = (HttpStatusCodeException) e;
			LOGGER.error("Nested error: {}", ce.getResponseBodyAsString());
		}
	}

}
