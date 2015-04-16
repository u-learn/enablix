package com.enablix.app.content.bounded.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.bounded.BoundedListManager;
import com.enablix.app.content.bounded.DataItem;

@RestController
@RequestMapping("bounded")
public class BoundedListController {

	@Autowired
	private BoundedListManager manager;
	
	@RequestMapping(method = RequestMethod.GET, value="/list/{templateId}/{contentQId}", produces = "application/json")
	public Collection<DataItem> fetchContainerNames(@PathVariable String templateId, 
			@PathVariable String contentQId) {
		return manager.getBoundedList(templateId, contentQId);
	}
	
}
