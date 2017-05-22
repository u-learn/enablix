package com.enablix.doc.converter.word;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.impl.MultiStepDocConverter;
import com.enablix.doc.converter.impl.MultiStepDocConverter.ConversionStep;

@Configuration
public class WordToImageConverter {

	@Bean
	public MultiStepDocConverter docxToImageConverter() {
		
		List<ConversionStep> conversionSteps = new ArrayList<>();
		conversionSteps.add(new ConversionStep(DocumentFormat.DOCX, DocumentFormat.PDF));
		conversionSteps.add(new ConversionStep(DocumentFormat.PDF, DocumentFormat.PNG));
		
		MultiStepDocConverter docxToImageConverter = new MultiStepDocConverter(conversionSteps );
		
		return docxToImageConverter;
	}
	
}
