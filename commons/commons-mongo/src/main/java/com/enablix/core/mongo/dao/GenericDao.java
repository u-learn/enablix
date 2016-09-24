package com.enablix.core.mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.core.mongo.search.service.SearchRequest.Pagination;
import com.enablix.core.mongo.search.service.SearchRequestTransformer;

@Component
public class GenericDao extends BaseDao {

	@Autowired
	private SearchRequestTransformer requestTx;

	public <T> Page<T> findByQuery(SearchRequest searchRequest, String collectionName, Class<T> findType) {
		
		Assert.notNull(findType, "findType is null");
		
		Criteria criteria = requestTx.buildQueryCriteria(searchRequest);
		
		Page<T> page = null;
		
		Pagination pagination = searchRequest.getPagination();
		Pageable pageable = null;
		
		if (pagination != null) {
			
			pageable = pagination.toPageableObject();
			page = findByCriteria(criteria, collectionName, findType, pageable);
			
		} else {
			List<T> content = findByCriteria(criteria, collectionName, findType);
			page = new PageImpl<T>(content, pageable, 0);
		}
		
		return page;
	}
	
	public <T> Page<T> findByQuery(SearchRequest searchRequest, Class<T> findType) {
		return findByQuery(searchRequest, null, findType);
	}
	
}
