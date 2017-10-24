package com.enablix.data.segment.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.segment.repo.DataSegmentRepository;

@RestController
@RequestMapping("ds")
public class DataSegmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSegmentController.class);
	
	@Autowired
	private DataSegmentService dsService;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private DataSegmentRepository dsRepo;
	
	@RequestMapping(method = RequestMethod.POST, value="/save", consumes = "application/json")
	public String saveDataSegment(@RequestBody DataSegment ds) throws IOException, JAXBException {
		LOGGER.debug("Saving data segment");
		dsService.save(ds);
		return "success";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/user/{dataSegmentIdentity}/{userEmailId}/")
	public String updateUserDataSegment(@PathVariable String dataSegmentIdentity, 
			@PathVariable String userEmailId) throws ServletException, IOException {

		UserProfile user = userProfileRepo.findByEmail(userEmailId);
		
		if (user != null) {
		
			DataSegment dataSegment = dsRepo.findByIdentity(dataSegmentIdentity);
			user.getSystemProfile().setDataSegment(dataSegment);
			
			userProfileRepo.save(user);
			
		} else {
			return "User not found!";
		}
		
		return "Updated successfully";
	}
	
}
