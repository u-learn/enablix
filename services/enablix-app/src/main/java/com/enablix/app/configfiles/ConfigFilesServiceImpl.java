package com.enablix.app.configfiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;

@Component
public class ConfigFilesServiceImpl implements ConfigFilesService {

	@Value("${baseDir}")
	private String baseDir;
	
	@Value("${dataDir:#{null}}")
	private String dataDir;
	
	@Override
	public void saveFile(String filetype, MultipartFile file) throws IOException {
		
		File outFile = new File(createFilePath(filetype, file.getOriginalFilename()));
		if (outFile.exists()) {
			outFile.delete();
		}
		
		outFile.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(outFile); 
		FileCopyUtils.copy(file.getInputStream(), fos);
		
	}
	
	private String createFilePath(String filetype, String filename) {
		String tenantId = ProcessContext.get().getTenantId();
		return getDataDirPath() + File.separator + filetype + File.separator + tenantId + File.separator + filename;
	}
	
	private String getDataDirPath() {
		return !StringUtil.isEmpty(dataDir) ? dataDir : baseDir;
	}

}
