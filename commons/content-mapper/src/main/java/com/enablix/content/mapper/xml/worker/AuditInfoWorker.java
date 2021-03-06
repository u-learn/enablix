package com.enablix.content.mapper.xml.worker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.content.ContentParser;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.content.mapper.xml.MappingWorker;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.SytemUserMappingType;
import com.enablix.core.domain.user.User;
import com.enablix.core.mongo.dao.GenericSystemDao;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class AuditInfoWorker implements MappingWorker {

	@Autowired
	private GenericSystemDao dao;
	
	@Override
	public float executionOrder() {
		return MappingWorkerExecOrder.AUDIT_INFO_WORKER;
	}

	@Override
	public void execute(ContentContainerMappingType containerMapping, ExternalContent extContent,
			EnablixContent ebxContent, TemplateFacade template) {
		
		findAndSetUser(extContent, ebxContent, containerMapping.getCreatedBy(), ContentDataConstants.CREATED_BY_KEY);
		findAndSetUser(extContent, ebxContent, containerMapping.getModifiedBy(), ContentDataConstants.MODIFIED_BY_KEY);
		
	}

	private void findAndSetUser(ExternalContent extContent,
			EnablixContent ebxContent, SytemUserMappingType userMapping, String userAttrKey) {
		
		if (userMapping != null) {
			Object creator = ContentParser.getSingleValue(extContent.getData(), userMapping.getValue());
			
			if (creator != null) {
				
				StringFilter userFilter = new StringFilter(
						userMapping.getAttribute(), String.valueOf(creator), ConditionOperator.EQ);
				
				List<User> users = dao.findByCriteria(userFilter.toPredicate(new Criteria()), User.class, MongoDataView.ALL_DATA);
				
				if (CollectionUtil.isNotEmpty(users)) {
					User createdByUser = users.get(0);
					ebxContent.getData().put(userAttrKey, createdByUser.getUserId());
				}
			}

		}
	}

}
