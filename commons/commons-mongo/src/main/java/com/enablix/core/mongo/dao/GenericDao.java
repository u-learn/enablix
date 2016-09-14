package com.enablix.core.mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.core.mongo.search.service.SearchRequest.Pagination;
import com.enablix.core.mongo.search.service.SearchRequestTransformer;

@Component
public class GenericDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private SearchRequestTransformer requestTx;

	public <T> Page<T> findByQuery(SearchRequest searchRequest, String collectionName, Class<T> findType) {
		
		Assert.notNull(findType, "findType is null");
		
		Criteria criteria = requestTx.buildQueryCriteria(searchRequest);
		Query query = Query.query(criteria);
		
		long count = 0;
		
		Pagination pagination = searchRequest.getPagination();
		Pageable pageable = null;
		
		if (pagination != null) {
			pageable = pagination.toPageableObject();
			count = mongoTemplate.count(query, findType);
			query = query.with(pageable);
		}
		
		List<T> records = StringUtil.isEmpty(collectionName) ? mongoTemplate.find(query, findType) 
				: mongoTemplate.find(query, findType, collectionName);
		
		return new PageImpl<T>(records, pageable, count);
	}
	
	public <T> Page<T> findByQuery(SearchRequest searchRequest, Class<T> findType) {
		return findByQuery(searchRequest, null, findType);
	}
	
}
