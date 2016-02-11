package com.enablix.app.content.enrich;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.id.IdentityGenerator;
import com.enablix.core.api.IdentityAware;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.BaseEntity;
import com.mongodb.DBObject;

@Component
public class ContainerIdentitySetter extends AbstractMongoEventListener<BaseEntity> implements ContentEnricher {

	@Autowired
	private IdentityGenerator identityGenerator;
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, ContentTemplate contentTemplate) {
		setIdentityInHierarchy(content);
	}
	
	private void setIdentityInHierarchy(final Map<String, Object> containerData) {
		
		setIdentity(new IdentityAware() {

			@Override
			public void setIdentity(String identity) {
				containerData.put(ContentDataConstants.IDENTITY_KEY, identity);
			}

			@Override
			public String getIdentity() {
				Object identity = containerData.get(ContentDataConstants.IDENTITY_KEY);
				return identity != null ? String.valueOf(identity) : null;
			}
			
		});
		
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
	
	private void setIdentity(IdentityAware identityAware) {
		Object obj = identityAware.getIdentity();
		if (obj == null) {
			identityAware.setIdentity(identityGenerator.generateId(identityAware));
		}
	}
	
	@Override
	public void onBeforeConvert(final BaseEntity source) {
		setIdentity(source);
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
