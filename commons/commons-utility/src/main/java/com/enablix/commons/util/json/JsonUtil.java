package com.enablix.commons.util.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.enablix.commons.content.ContentParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
	
	public static Object getJsonpathValue(String jsonStr, String jsonpath) {
		Map<String, Object> jsonToMap = jsonToMap(jsonStr);
		return ContentParser.getSingleValue(jsonToMap, jsonpath);
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

	public static JsonNode getJSONNode(String jsonData){
		try{
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.readTree(jsonData);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to convert json to Map", e);
		}
	}
	
	public static <T> T jsonToJavaPojo(JsonNode json, Class<T> className) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.treeToValue(json, className);
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert json to Map", e);
		}
	}
	
	public static <T> T jsonToObject(Map<String, Object> json, Class<T> objectType) {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(json, objectType);
	}

	/*public static void main(String[] args) {
		String json = "{\"name\" : \"abc\" ,"
			+ "\"email id\" : [\"abc@gmail.com\",\"def@gmail.com\",\"ghi@gmail.com\"],"
			+ "\"abc\" : {\"b\":\"b1\", \"c\":\"c1\"}"
			+ "}";
		System.out.println(jsonToMap(json));
	}*/
}