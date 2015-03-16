package com.enablix.app.content.enrich;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataConstants;
import com.enablix.commons.util.id.IdentityGenerator;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@Component
public class ContainerIdentitySetter implements ContentEnricher {

	@Autowired
	private IdentityGenerator identityGenerator;
	
	@Override
	public void enrich(Map<String, Object> content, ContentTemplate contentTemplate) {
		setIdentityInHierarchy(content);
	}
	
	private void setIdentityInHierarchy(Map<String, Object> containerData) {
		
		setIdentity(containerData);
		
		// populate identity in hierarchy
		for (Map.Entry<String, Object> entry : containerData.entrySet()) {
			setIdentityIfContainer(entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setIdentityIfContainer(Object obj) {
		
		if (obj instanceof Map<?,?>) {
			Map<String, Object> mapObj = (Map<String, Object>) obj; 
			setIdentityInHierarchy(mapObj);
			
		} else if (obj instanceof Collection<?>) {
			for (Object collElement : (Collection<?>) obj) {
				setIdentityIfContainer(collElement);
			}
		}
		
	}
	
	private void setIdentity(Map<String, Object> containerData) {
		Object obj = containerData.get(ContentDataConstants.IDENTITY_KEY);
		if (obj == null) {
			containerData.put(ContentDataConstants.IDENTITY_KEY, 
					identityGenerator.generateId(containerData));
		}
	}

	/*public static void main(String[] args) {
		String json = "{\"name\" : \"abc\" ,"
			+ "\"email id\" : [\"abc@gmail.com\",\"def@gmail.com\",\"ghi@gmail.com\"],"
			+ "\"abc\" : {\"b\":\"b1\", \"c\":\"c1\"},"
			+ "\"abc1\" : [{\"b\":\"b1\", \"c\":\"c1\"},{\"b\":\"b2\", \"c\":\"c2\"}]"
			+ "}";
		ContainerIdentitySetter idSetter = new ContainerIdentitySetter();
		idSetter.identityGenerator = new UUIDIdentityGenerator();
		Map<String, Object> jsonToMap = JsonUtil.jsonToMap(json);
		idSetter.enrich(jsonToMap, null);
		System.out.println(jsonToMap);
	}*/
	
}
