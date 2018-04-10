package com.enablix.uri.embed.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.EmbedException;
import com.enablix.uri.embed.EmbedService;

@RestController
@RequestMapping("urlembed")
public class UrlEmbedController {

	@Autowired
	private EmbedService service;
	
	@RequestMapping(method = RequestMethod.GET, value="/fetch/", produces = "application/json")
	public EmbedInfo getContentRequest(@RequestParam(required=true) String url) throws EmbedException {
		return service.getEmbedInfo(url);
	}
	
}
