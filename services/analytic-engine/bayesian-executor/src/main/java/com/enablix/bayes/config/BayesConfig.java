package com.enablix.bayes.config;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BayesConfig {

	@Bean
	public <T> FlatFileItemWriter<T> csvWriter() {
		FlatFileItemWriter<T> csvWriter = new FlatFileItemWriter<T>();
		csvWriter.setLineAggregator(csvLineAggregator());
		return csvWriter;
	}

	@Bean
	public <T> LineAggregator<T> csvLineAggregator() {
		DelimitedLineAggregator<T> lineAggregator = new DelimitedLineAggregator<>();
		return lineAggregator;
	}
	
}
