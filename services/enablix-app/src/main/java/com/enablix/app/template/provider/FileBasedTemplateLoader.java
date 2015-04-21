package com.enablix.app.template.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.dir.watch.DirectoryWatchBuilder;
import com.enablix.commons.dir.watch.FileCreateOrUpdateCallback;

@Component
public class FileBasedTemplateLoader implements TemplateLoader {

	private static final String[] TEMPLATE_FILE_EXTN = {"xml"};
	
	@Value("${baseDir}")
	private String baseDirPath;
	
	@Autowired
	private TemplateManager templateManager;
	
	public String getTemplateDirectory() {
		return baseDirPath + File.separator + "templates";
	}

	@Override
	public void loadAllTemplates() {
		
		String templateDirPath = getTemplateDirectory();
		File templateDir = new File(templateDirPath);
		
		for (File templateFile : FileUtils.listFiles(templateDir, TEMPLATE_FILE_EXTN, false)) {
			uploadTemplate(templateFile);
		}
		
		createWatch();
	}

	private void uploadTemplate(File templateFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(templateFile);
			templateManager.saveXml(fis);
			
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
			
		} finally {
			
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}
	
	
	
	private void createWatch() {
		
		FilenameFilter fileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		};
		
		DirectoryWatchBuilder.createDirectoryWatch(
				getTemplateDirectory(), new UploadTemplateCallback())
				.forFiles(fileFilter).build();
	}

	private class UploadTemplateCallback implements FileCreateOrUpdateCallback {

		@Override
		public void onFileCreated(File fileName) {
			uploadTemplate(fileName);
			
		}

		@Override
		public void onFileUpdated(File fileName) {
			uploadTemplate(fileName);
		}
		
	}
	
}
