package com.enablix.core.mongo.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.api.IdentityAware;

@Component
public class IdentitySetter extends AbstractMongoEventListener<IdentityAware> {

	public void onBeforeConvert(BeforeConvertEvent<IdentityAware> event) {
		if (StringUtil.isEmpty(event.getSource().getIdentity())) {
			event.getSource().setIdentity(IdentityUtil.generateIdentity(event.getSource()));
		}
		
	}
}
