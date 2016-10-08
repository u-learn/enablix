package com.enablix.commons.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ContentParseExpression {

	private String[] path;
	
	public ContentParseExpression(String parseExp) {
		this.path = parseExp.split("\\.");
	}
	
	public List<Object> evaluate(Map<String, Object> data) {
		return evaluatePath(data, this.path);
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> evaluatePath(Map<String, Object> data, String[] path) {
		
		List<Object> values = new ArrayList<>();
		
		if (path.length > 0 && data != null) {
			
			Object pathValue = data.get(path[0]);
			
			if (pathValue != null) {
				
				if (path.length == 1) {
					
					if (pathValue instanceof Collection<?>) {
						values.addAll((Collection<?>) pathValue);
					} else {
						values.add(pathValue);
					}
					
				} else if (path.length > 1) {
					
					String[] remainingPath = Arrays.copyOfRange(path, 1, path.length);
					
					if (pathValue instanceof Collection<?>) {
					
						Collection<?> listValue = (Collection<?>) pathValue;
						
						for (Object val : listValue) {
							
							if (val instanceof Map<?, ?>) {
								List<Object> deptValues = evaluatePath((Map<String, Object>) val, remainingPath);
								if (deptValues != null) {
									values.addAll(deptValues);
								}
							}
						}
						
					} else if (pathValue instanceof Map<?, ?>) {
						List<Object> deptValues = evaluatePath((Map<String, Object>) pathValue, remainingPath);
						if (deptValues != null) {
							values.addAll(deptValues);
						}
					}
				}
			}
		}
		
		return values;
	}
	
}
