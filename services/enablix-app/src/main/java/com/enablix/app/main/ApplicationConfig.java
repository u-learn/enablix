package com.enablix.app.main;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.app.template.provider.TemplateFileProcessor;
import com.enablix.commons.util.StringUtil;
import com.enablix.refdata.xls.loader.XLSRefdataProcessor;
import com.enablix.util.data.loader.FileBasedDataLoader;

@Configuration
public class ApplicationConfig {

	private static final String TEMPLATES_DIR = "templates";
	
	private static final String[] TEMPLATE_FILE_EXTN = {"xml"};
	
	private static final String REFDATA_DIR = "refdata";
	
	private static final String[] REFDATA_FILE_EXTN = {"xlsx"};
	
	@Value("${baseDir}")
	private String baseDir;
	
	@Value("${dataDir}")
	private String dataDir;
	
	@Bean
	public TemplateFileProcessor templateFileProcessor() {
		return new TemplateFileProcessor();
	}
	
	@Bean
	public FileBasedDataLoader templateFilesLoader() {
		String templatesBaseDir = getDataDirPath() + File.separator + TEMPLATES_DIR;
		return new FileBasedDataLoader(templatesBaseDir, TEMPLATE_FILE_EXTN, templateFileProcessor());
	}

	private String getDataDirPath() {
		return !StringUtil.isEmpty(dataDir) ? dataDir : baseDir;
	}
	
	
	@Bean
	public XLSRefdataProcessor refdataFileProcessor() {
		return new XLSRefdataProcessor();
	}
	
	@Bean
	public FileBasedDataLoader refdataFilesLoader() {
		String refdataBaseDir = getDataDirPath() + File.separator + REFDATA_DIR;
		return new FileBasedDataLoader(refdataBaseDir, REFDATA_FILE_EXTN, refdataFileProcessor());
	}
	
}
