package com.enablix.commons.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static String toJsonString(final Object objToSerialize) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(objToSerialize);
		} catch (final Exception e) {
			throw new RuntimeException("Failed to perform json serialization", e);
		}
	}
}
