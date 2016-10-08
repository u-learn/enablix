package com.enablix.commons.content;

import java.util.List;
import java.util.Map;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.json.JsonUtil;

public class ContentParser {

	/**
	 * Parses the map to extract value for the given expression
	 * The expression in the form of "a.b.c" would traverse the map
	 * looking for property a, whose value is expected to be a map 
	 * or list of map and so on.
	 * 
	 * @param in
	 * @param propExpression
	 * @return
	 */
	public static List<Object> getValue(Map<String, Object> data, String propExpression) {
		ContentParseExpression exp = new ContentParseExpression(propExpression);
		return exp.evaluate(data);
	}
	
	public static Object getSingleValue(Map<String, Object> data, String propExpression) {
		
		List<Object> listVal = getValue(data, propExpression);
		
		if (CollectionUtil.isNotEmpty(listVal)) {
			
			if (listVal.size() > 1) {
				throw new IllegalStateException("Expected one value but found: " + listVal);
			}
			
			return listVal.get(0);
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		
		String json = "{\"attributes\":{\"type\":\"Opportunity\",\"url\":\"/services/data/v37.0/sobjects/Opportunity/006o000000MaYIsAAN\"},"
				+ "\"Id\":\"006o000000MaYIsAAN\",\"LastModifiedDate\":\"2016-10-01T09:09:34.000+0000\",\"IsDeleted\":false,"
				+ "\"AccountId\":\"001o000000WpaiQAAR\",\"StageName\":\"Prospecting\",\"Probability\":10,\"ForecastCategory\":\"Pipeline\","
				+ "\"IsClosed\":false,\"CloseDate\":\"2016-10-02\",\"Name\":\"Test Creation7\",\"SystemModstamp\":\"2016-10-01T09:09:34.000+0000\","
				+ "\"CreatedById\":\"005o0000001hI3SAAU\",\"OwnerId\":\"005o0000001hI3SAAU\",\"IsWon\":false,\"CreatedDate\":\"2016-10-01T09:09:34.000+0000\","
				+ "\"Org__c\":\"Zafin\",\"ForecastCategoryName\":\"Pipeline\",\"IsPrivate\":false,\"LastModifiedById\":\"005o0000001hI3SAAU\","
				+ "\"Solution__c\":\"Retail CLM\",\"Competitor__c\":\"Barclays\","
				+ "\"Account\":{\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v37.0/sobjects/Account/001o000000WpaiQAAR\"},"
				+ "\"Name\":\"Bank of America\",\"Id\":\"001o000000WpaiQAAR\"},"
				+ "\"CreatedBy\":{\"attributes\":{\"type\":\"User\",\"url\":\"/services/data/v37.0/sobjects/User/005o0000001hI3SAAU\"},"
				+ "\"Name\":\"Gaurav Harode\",\"Id\":\"005o0000001hI3SAAU\"},"
				+ "\"Owner\":{\"attributes\":{\"type\":\"User\",\"url\":\"/services/data/v37.0/sobjects/User/005o0000001hI3SAAU\"},"
				+ "\"Name\":\"Gaurav Harode\",\"Id\":\"005o0000001hI3SAAU\"},"
				+ "\"Nested\":[{\"prop1\":[{\"prop2\":\"val1\"},{\"prop2\":\"val2\"}]},{\"prop1\":[{\"prop2\":\"val3\"},{\"prop2\":\"val4\"}]}],"
				+ "\"LastModifiedBy\":{\"attributes\":{\"type\":\"User\",\"url\":\"/services/data/v37.0/sobjects/User/005o0000001hI3SAAU\"},"
				+ "\"Name\":\"Gaurav Harode\",\"Id\":\"005o0000001hI3SAAU\"}}";
		
		Map<String, Object> map = JsonUtil.jsonToMap(json);
		String spel = "Nested.prop1.prop2";
		
		System.out.println(ContentParser.getValue(map, spel));
	}
	
}
