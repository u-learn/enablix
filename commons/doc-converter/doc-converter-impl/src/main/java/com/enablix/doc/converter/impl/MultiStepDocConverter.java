package com.enablix.doc.converter.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.enablix.commons.dir.watch.SystemTempFolderResolver;
import com.enablix.commons.util.IOUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocConverterFactory;
import com.enablix.doc.converter.DocumentStream;

public class MultiStepDocConverter implements DocConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiStepDocConverter.class);
	
	@Autowired
	private SystemTempFolderResolver systemTempFolderResolver;
	
	@Autowired
	private DocConverterFactory docConverterFactory;
	
	private List<ConversionStep> steps;
	
	private DocumentFormat inFormat;
	
	private DocumentFormat outFormat;

	public MultiStepDocConverter(List<ConversionStep> conversionSteps) {
		super();
		Assert.isTrue(conversionSteps.size() > 1, "Conversion step count should be > 1");
		
		inFormat = conversionSteps.get(0).inFormat;
		outFormat = conversionSteps.get(conversionSteps.size() - 1).outFormat;
		
		this.steps = conversionSteps;
	}

	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat) throws IOException {
		
		String tmpFolder = getTempFolderLocation();
		
		InputStream workingIS = is;
		File tempFile = null;
		
		for (int i = 0; i < steps.size(); i++) {
			
			ConversionStep currentStep = steps.get(i);
			
			FileInputStream tempFis = null;
			if (tempFile != null) {
				tempFis = new FileInputStream(tempFile);
				workingIS = tempFis;
			}
			
			File newTempFile = null;
			
			try {
				
				if (i < steps.size() - 1) { // not last step
					
					String nextTempFile = IdentityUtil.generateIdentity(tmpFolder);
					TempDiskLocDocumentStream currentTempDS = new TempDiskLocDocumentStream(tmpFolder, nextTempFile);
					
					executeConversion(workingIS, currentTempDS, currentStep);
					
					newTempFile = currentTempDS.getOutFile();
					
				} else {
					executeConversion(workingIS, os, currentStep);
				}
				
			} finally {
				IOUtil.closeStream(tempFis);
				IOUtil.deleteFile(tempFile);
			}
			
			tempFile = newTempFile;
			
		}
		
	}
	
	private String getTempFolderLocation() throws IOException {
		
		String tmpFolderLoc = systemTempFolderResolver.getSystemTempFolder();
		
		File tmpFolder = new File(tmpFolderLoc);
		
		if (!tmpFolder.exists()) {
			boolean dirCreated = tmpFolder.mkdirs();
			if (!dirCreated) {
				throw new IOException("Unable to create folder structure: " + tmpFolderLoc);
			}
		}
		
		return tmpFolderLoc;
	}
	
	public void executeConversion(InputStream is, DocumentStream os, ConversionStep conversionStep) throws IOException {
		
		DocumentFormat cInFormat = conversionStep.inFormat;
		DocumentFormat cOutFormat = conversionStep.outFormat;
		
		DocConverter converter = docConverterFactory.getConverter(cInFormat, cOutFormat);
		
		if (converter == null) {
			
			LOGGER.error("Doc converter not found for [{}] -> [{}] conversion", cInFormat, cOutFormat);
			
		} else {
			converter.convertAndWrite(is, cInFormat, os, cOutFormat);
		}
		
	}

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		return from == inFormat && to == outFormat;
	}
	
	public static class ConversionStep {
		
		private DocumentFormat inFormat;
		private DocumentFormat outFormat;
		
		public ConversionStep(DocumentFormat inFormat, DocumentFormat outFormat) {
			super();
			this.inFormat = inFormat;
			this.outFormat = outFormat;
		}
		
	}

}