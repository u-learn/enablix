package com.enablix.app.main;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.app.template.provider.TemplateFileProcessor;
import com.enablix.util.data.loader.FileBasedDataLoader;

@Configuration
public class ApplicationConfig {

	private static final String TEMPLATES_DIR = "templates";
	
	private static final String[] TEMPLATE_FILE_EXTN = {"xml"};
	
	@Value("${baseDir}")
	private String baseDir;
	
	@Bean
	public TemplateFileProcessor templateFileProcessor() {
		return new TemplateFileProcessor();
	}
	
	@Bean
	public FileBasedDataLoader templateFilesLoader() {
		String templatesBaseDir = baseDir + File.separator + TEMPLATES_DIR;
		return new FileBasedDataLoader(templatesBaseDir, TEMPLATE_FILE_EXTN, templateFileProcessor());
	}
	
}
