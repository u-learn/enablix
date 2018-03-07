package com.enablix.commons.dir.watch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryWatchBuilder {

	private DefaultDirectoryWatcher watcher;
	
	private DirectoryWatchBuilder(String directory, FileCreateOrUpdateCallback callback) {
		this.watcher = new DefaultDirectoryWatcher(directory);
		this.watcher.setFileCreateOrUpdateCallback(callback);
	}
	
	public DirectoryWatchBuilder forFiles(FilenameFilter filter) {
		this.watcher.setFilenameFilter(filter);
		return this;
	}
	
	public DirectoryWatcher build() {
		this.watcher.start();
		return this.watcher;
	}

	public static DirectoryWatchBuilder createDirectoryWatch(
			String directory, FileCreateOrUpdateCallback callback) {
		return new DirectoryWatchBuilder(directory, callback);
	}

	private static class DefaultDirectoryWatcher implements DirectoryWatcher {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDirectoryWatcher.class);

		private Path directory;
		
		private FilenameFilter fileFilter;
		
		private FileCreateOrUpdateCallback callback;
		
		public DefaultDirectoryWatcher(String directory) {
			this.directory = Paths.get(directory);
		}

		@Override
		public void setFilenameFilter(FilenameFilter filter) {
			this.fileFilter = filter;
		}

		@Override
		public void setFileCreateOrUpdateCallback(FileCreateOrUpdateCallback callback) {
			this.callback = callback;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void start() {
			
			Thread watcherThread = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						
			            WatchService fileSystemWatchService = FileSystems.getDefault().newWatchService();
			            
			            directory.register(fileSystemWatchService,
			                    StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY,
			                    StandardWatchEventKinds.ENTRY_DELETE);
			            
			            while(true) {
			                
			            	WatchKey watchKey = fileSystemWatchService.take();
			            	
			                for (WatchEvent<?> event: watchKey.pollEvents()) {
			                
			                	WatchEvent.Kind<?> eventKind = event.kind();
			 
			                    if (eventKind == StandardWatchEventKinds.OVERFLOW){
			                        continue;
			                    }
			 
			                    WatchEvent<Path> eventPath = (WatchEvent<Path>) event;
			                    Path fileName = eventPath.context();

			                    LOGGER.debug("Event {} occured on {}", eventKind, fileName);
			                    
			                    File file = new File(directory.toString() + File.separator + fileName.toString());
			                    
			                    if (fileFilter.accept(file.getParentFile(), file.getName())) {
				                    
			                    	if (eventKind == StandardWatchEventKinds.ENTRY_CREATE) {
				                    	callback.onFileCreated(file);
				                    	
				                    } else if (eventKind == StandardWatchEventKinds.ENTRY_MODIFY) {
				                    	callback.onFileUpdated(file);
				                    }

			                    }
			                    
			                }
			                
			                boolean isReset = watchKey.reset();

			                if (!isReset) {
			                    break;
			                }
			                
			            }
			            
			        } catch (IOException | InterruptedException e) {
			        	LOGGER.error("Error in directory watcher", e);
			        } 
					
				}
				
			});
			
			watcherThread.start();
		}
		
	}
	
}
