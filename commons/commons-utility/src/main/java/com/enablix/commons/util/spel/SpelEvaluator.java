package com.enablix.commons.util.spel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.json.JsonUtil;

@Component
public class SpelEvaluator {

	private ExpressionParser parser;
	
	private Map<String, Expression> expressionCache;
	
	public SpelEvaluator() {
		this.parser = new SpelExpressionParser();
		this.expressionCache = new HashMap<>();
	}
	
	public Object evaluate(Object in, String spel) {
		Expression expression = getExpression(spel);
		return expression.getValue(in);
	}
	
	public Expression getExpression(String spel) {
		Expression expression = expressionCache.get(spel);
		if (expression == null) {
			expression = parser.parseExpression(spel);
			expressionCache.put(spel, expression);
		}
		return expression;
	}
	
	public static void main(String[] args) {
		
		String json = "{\"attributes\":{\"type\":\"Opportunity\",\"url\":\"/services/data/v37.0/sobjects/Opportunity/006o000000MaYIsAAN\"},\"Id\":\"006o000000MaYIsAAN\",\"LastModifiedDate\":\"2016-10-01T09:09:34.000+0000\",\"IsDeleted\":false,\"AccountId\":\"001o000000WpaiQAAR\",\"StageName\":\"Prospecting\",\"Probability\":10,\"ForecastCategory\":\"Pipeline\",\"IsClosed\":false,\"CloseDate\":\"2016-10-02\",\"Name\":\"Test Creation7\",\"SystemModstamp\":\"2016-10-01T09:09:34.000+0000\",\"CreatedById\":\"005o0000001hI3SAAU\",\"OwnerId\":\"005o0000001hI3SAAU\",\"IsWon\":false,\"CreatedDate\":\"2016-10-01T09:09:34.000+0000\",\"Org__c\":\"Zafin\",\"ForecastCategoryName\":\"Pipeline\",\"IsPrivate\":false,\"LastModifiedById\":\"005o0000001hI3SAAU\",\"Solution__c\":\"Retail CLM\",\"Competitor__c\":\"Barclays\",\"Account\":{\"attributes\":{\"type\":\"Account\",\"url\":\"/services/data/v37.0/sobjects/Account/001o000000WpaiQAAR\"},\"Name\":\"Bank of America\",\"Id\":\"001o000000WpaiQAAR\"},\"CreatedBy\":{\"attributes\":{\"type\":\"User\",\"url\":\"/services/data/v37.0/sobjects/User/005o0000001hI3SAAU\"},\"Name\":\"Gaurav Harode\",\"Id\":\"005o0000001hI3SAAU\"},\"Owner\":{\"attributes\":{\"type\":\"User\",\"url\":\"/services/data/v37.0/sobjects/User/005o0000001hI3SAAU\"},\"Name\":\"Gaurav Harode\",\"Id\":\"005o0000001hI3SAAU\"},\"LastModifiedBy\":{\"attributes\":{\"type\":\"User\",\"url\":\"/services/data/v37.0/sobjects/User/005o0000001hI3SAAU\"},\"Name\":\"Gaurav Harode\",\"Id\":\"005o0000001hI3SAAU\"}}";
		Map<String, Object> map = JsonUtil.jsonToMap(json);
		String spel = "[Account][Name]";
		
		SpelEvaluator evaluator = new SpelEvaluator();
		System.out.println(evaluator.evaluate(map, spel));
	}
	
}
