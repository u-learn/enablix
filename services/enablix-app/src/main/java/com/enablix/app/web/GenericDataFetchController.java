package com.enablix.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.service.SearchRequest;

@RestController
@RequestMapping("/data/search")
public class GenericDataFetchController {

	@Autowired
	private GenericDao dao;
	
	@RequestMapping(method = RequestMethod.POST, value="/c/{collectionName}/t/{className}",
			consumes = "application/json", produces = "application/json")
	public Page<?> filterData(@RequestBody SearchRequest searchRequest,
			@PathVariable String collectionName, @PathVariable String className) throws ClassNotFoundException {
		Class<?> findType = Class.forName(className);
		return dao.findByQuery(searchRequest, collectionName, findType);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/t/{className}",
			consumes = "application/json", produces = "application/json")
	public Page<?> filterData(@RequestBody SearchRequest searchRequest,
			@PathVariable String className) throws ClassNotFoundException {
		Class<?> findType = Class.forName(className);
		return dao.findByQuery(searchRequest, findType);
	}
	
}
