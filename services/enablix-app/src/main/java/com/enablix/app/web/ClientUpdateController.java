package com.enablix.app.web;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.link.QuickLinksService;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.domain.tenant.TenantClient;
import com.enablix.core.security.auth.repo.ClientRepository;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.repo.DataSegmentRepository;

@RestController
@RequestMapping("/client")
public class ClientUpdateController {
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private DataSegmentRepository dsRepo;
	
	@Autowired
	private QuickLinksService quickLinksService;
	
	@Autowired
	private ClientRepository clientRepo;
	
	@RequestMapping(method = RequestMethod.GET, value="/create/{clientId}/{clientName}/")
	public String createClient(@PathVariable String clientId, 
			@PathVariable String clientName) throws ServletException, IOException {

		TenantClient client = new TenantClient();
		client.setClientId(clientId);
		client.setId(clientId);
		client.setIdentity(clientId);
		client.setClientName(clientName);
		
		clientRepo.save(client);
		
		return "Updated successfully";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/user/{clientId}/{dataSegmentIdentity}/{userEmailId}/")
	public String updateUserClientIdAndDataSegment(@PathVariable String clientId, 
			@PathVariable String dataSegmentIdentity, @PathVariable String userEmailId) throws ServletException, IOException {

		UserProfile user = userProfileRepo.findByEmail(userEmailId);
		
		if (user != null) {
		
			DataSegment dataSegment = dsRepo.findByIdentity(dataSegmentIdentity);
			user.getSystemProfile().setDataSegment(dataSegment);
			user.getSystemProfile().setDefaultClientId(clientId);
			
			userProfileRepo.save(user);
			
		} else {
			return "User not found!";
		}
		
		return "Updated successfully";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/ql/{clientId}/{qlCategoryName}/")
	public String updateQuickLinkClientId(@PathVariable String clientId, 
			@PathVariable String qlCategoryName) throws ServletException, IOException {

		boolean updated = quickLinksService.updateCategoryClientId(qlCategoryName, clientId);
		
		if (!updated) {
			return "Unable to update";
		}
		
		return "Updated successfully";
	}
	
}
