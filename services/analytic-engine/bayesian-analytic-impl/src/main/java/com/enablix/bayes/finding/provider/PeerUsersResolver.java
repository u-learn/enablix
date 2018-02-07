package com.enablix.bayes.finding.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class PeerUsersResolver {
	
	@Autowired
	private GenericDao dao;

	@SuppressWarnings("unchecked")
	public List<String> getPeerUserIds(UserProfile user) {
		
		Map<String, Object> bizAttrs = user.getBusinessProfile().getAttributes();
		
		if (!CollectionUtil.isEmpty(bizAttrs)) {
			
			SearchFilter filter = new StringFilter("identity", user.getIdentity(), ConditionOperator.NOT_EQ);
			
			// TODO: make it configurable per tenant
			String bizRoleAttr = "role";
			
			Object userBizRoles = bizAttrs.get(bizRoleAttr);
	
			if (userBizRoles != null && userBizRoles instanceof Collection) {
				
				Collection<Map<String, Object>> attrValues = (Collection<Map<String, Object>>) userBizRoles;
				
				ArrayList<String> bizRoleIds = CollectionUtil.transform(attrValues, 
						() -> new ArrayList<String>(), (mapRec) -> (String) mapRec.get("id"));
				
				StringListFilter attrFilter = new StringListFilter(
						"businessProfile.attributes." + bizRoleAttr + ".id", 
						bizRoleIds, ConditionOperator.IN);
				
				filter = filter.and(attrFilter);
				
				Criteria criteria = filter.toPredicate(new Criteria());
				List<UserProfile> peerUsers = dao.findByCriteria(criteria, UserProfile.class, MongoDataView.ALL_DATA);
				
				return CollectionUtil.transform(peerUsers, 
						() -> new ArrayList<String>(), (userProf) -> userProf.getEmail());
			}
		}
		
		return new ArrayList<>();
	}
	
}
