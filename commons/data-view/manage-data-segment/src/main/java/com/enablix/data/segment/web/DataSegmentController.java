package com.enablix.data.segment.web;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.segment.DataSegmentService;

@RestController
@RequestMapping("ds")
public class DataSegmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSegmentController.class);
	
	@Autowired
	private DataSegmentService dsService;
	
	@RequestMapping(method = RequestMethod.POST, value="/save", consumes = "application/json")
	public String saveDataSegment(@RequestBody DataSegment ds) throws IOException, JAXBException {
		LOGGER.debug("Saving data segment");
		dsService.save(ds);
		return "success";
	}
	
}
