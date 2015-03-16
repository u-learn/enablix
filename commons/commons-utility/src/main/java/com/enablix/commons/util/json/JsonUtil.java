package com.enablix.commons.util.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
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
	
	public static Map<String, Object> jsonToMap(String json) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		ObjectMapper mapper = new ObjectMapper();
	 
		try {
	 
			//convert JSON string to Map
			map = mapper.readValue(json, 
			    new TypeReference<HashMap<String, Object>>(){});
	 
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert json to Map", e);
		}
		
		return map;
	}
	
	/*public static void main(String[] args) {
		String json = "{\"name\" : \"abc\" ,"
			+ "\"email id\" : [\"abc@gmail.com\",\"def@gmail.com\",\"ghi@gmail.com\"],"
			+ "\"abc\" : {\"b\":\"b1\", \"c\":\"c1\"}"
			+ "}";
		System.out.println(jsonToMap(json));
	}*/
}
