package com.enablix.app.configfiles.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enablix.app.configfiles.ConfigFilesService;

@RestController
@RequestMapping("cfgfiles")
public class ConfigFileController {
	
	@Autowired
	private ConfigFilesService configFilesService;
	
	@RequestMapping(value = "/{filetype}", method = RequestMethod.POST)
    public String handleFileUpload(@PathVariable String filetype,
            @RequestParam(value="file", required = true) MultipartFile file) throws IOException {
        
		configFilesService.saveFile(filetype, file);
		return "SUCCESS";       
    }
	
}
