package com.enablix.util.data.loader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.enablix.commons.dir.watch.DirectoryWatchBuilder;
import com.enablix.commons.dir.watch.FileCreateOrUpdateCallback;

public class FileBasedDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedDataLoader.class);
	
	private String dataDirectory;
	
	private String[] fileExtensions;

	private DataFileProcessor dataFileProcessor;
	
	public FileBasedDataLoader(String dataDirectory, String[] fileExtns, DataFileProcessor fileProcessor) {
		this.dataDirectory = dataDirectory;
		this.fileExtensions = fileExtns;
		this.dataFileProcessor = fileProcessor;
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		loadAllData();
	}
	
	public void loadAllData() {
		
		LOGGER.debug("Loading data from directory [{}]", dataDirectory);
		
		File dataDir = new File(dataDirectory);
		
		Set<String> tenantDataDirs = new HashSet<>();

		for (File dataFile : FileUtils.listFiles(dataDir, fileExtensions, true)) {
		
			String tenantId = resolveTenantIdFromFile(dataFile);
			tenantDataDirs.add(dataDirectory + File.separator + tenantId);

			loadDataFile(dataFile);
		}
		
		// create watch on each directory
		for (String tenantDataDir : tenantDataDirs) {
			createWatch(tenantDataDir);
		}
		
	}

	protected void loadDataFile(File dataFile) {

		try {
			
			LOGGER.info("Loading data file: " + dataFile.getCanonicalPath());
			dataFileProcessor.process(dataFile);
			
		} catch (IOException e) {
			LOGGER.error("Error loading file [" + dataFile.getAbsolutePath() + "]", e);
		} 
	}

	private String resolveTenantIdFromFile(File templateFile) {
		return templateFile.getParentFile().getName();
	}
	
	private void createWatch(String templateDir) {
		
		FilenameFilter fileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				for (String fileExtn : fileExtensions) {
					if (name.endsWith(fileExtn)) {
						return true;
					}
				}
				return false;
			}
		};
		
		DirectoryWatchBuilder
			.createDirectoryWatch(templateDir, new UploadDataCallback())
			.forFiles(fileFilter)
			.build();
	}

	private class UploadDataCallback implements FileCreateOrUpdateCallback {

		@Override
		public void onFileCreated(File fileName) {
			loadDataFile(fileName);
			
		}

		@Override
		public void onFileUpdated(File fileName) {
			loadDataFile(fileName);
		}
		
	}
	
}
