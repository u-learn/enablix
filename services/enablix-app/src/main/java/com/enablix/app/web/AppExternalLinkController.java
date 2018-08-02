package com.enablix.app.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.services.util.ActivityLogger;

@RestController
public class AppExternalLinkController {
	
	@RequestMapping(method = RequestMethod.GET, value="/xlink")
	public void externalLink(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String u, // redirect to link
			@RequestParam String cId, // content instance identity
			@RequestParam String cQId, // content item QId
			@RequestParam(required=false) String atChannel,
			@RequestParam(required=false) String aid //actor identifier
			) throws ServletException, IOException {
		
		Channel channel = Channel.parse(atChannel);
		if (channel != null) {
			ActivityLogger.auditExternalLinkAccess(u, cId, cQId, aid, channel);
		}
		
		// redirect to the url
		u = u.startsWith("http") ? u : ("http://" + u);
		response.sendRedirect(u);
	}
	
}
