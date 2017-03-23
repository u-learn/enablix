package com.enablix.app.configfiles;

import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface ConfigFilesService {

	@PreAuthorize("hasAuthority('UPLOAD_CONFIG_FILES')")
	void saveFile(String filetype, MultipartFile file) throws IOException;
	
}
