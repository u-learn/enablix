package com.enablix.app.main;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.app.template.provider.TemplateFileProcessor;
import com.enablix.commons.util.StringUtil;
import com.enablix.refdata.xls.loader.XLSRefdataProcessor;
import com.enablix.util.data.loader.FileBasedDataLoader;
import com.enablix.util.data.loader.TenantSpecificFileBasedDataLoader;

@Configuration
public class ApplicationConfig {

	private static final String TEMPLATES_DIR = "templates";
	
	private static final String[] TEMPLATE_FILE_EXTN = {"xml"};
	
	private static final String REFDATA_DIR = "refdata";
	
	private static final String[] REFDATA_FILE_EXTN = {"xlsx"};

	private static final String DOCSTORE_CONFIG_DIR = "config/docstore";

	private static final String[] DOCSTORE_CONFIG_FILE_EXTN = {"json"};
	
	@Value("${baseDir}")
	private String baseDir;
	
	@Value("${dataDir:#{null}}")
	private String dataDir;
	
	@Bean
	public TemplateFileProcessor templateFileProcessor() {
		return new TemplateFileProcessor();
	}
	
	@Bean
	public FileBasedDataLoader templateFilesLoader() {
		String templatesBaseDir = getDataDirPath() + File.separator + TEMPLATES_DIR;
		return new TenantSpecificFileBasedDataLoader(templatesBaseDir, TEMPLATE_FILE_EXTN, templateFileProcessor());
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
		return new TenantSpecificFileBasedDataLoader(refdataBaseDir, REFDATA_FILE_EXTN, refdataFileProcessor());
	}
	
	
	/*@Bean
	public JsonFileDocStoreConfigMetadataProvider jsonDocStoreConfigMetadataProcessor() {
		return new JsonFileDocStoreConfigMetadataProvider();
	}
	
	@Bean
	public FileBasedDataLoader jsonDocStoreConfigMetadataFilesLoader() {
		String refdataBaseDir = baseDir + File.separator + DOCSTORE_CONFIG_DIR;
		return new FileBasedDataLoader(refdataBaseDir, DOCSTORE_CONFIG_FILE_EXTN, jsonDocStoreConfigMetadataProcessor());
	}*/
	
	
}
