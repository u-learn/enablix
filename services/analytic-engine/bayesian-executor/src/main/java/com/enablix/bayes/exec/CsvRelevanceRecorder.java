package com.enablix.bayes.exec;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.core.io.FileSystemResource;

import com.enablix.bayes.EBXNet;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.RelevanceRecorder;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.content.UserContentRelevance;
import com.enablix.core.domain.content.UserContentRelevance.NodeRelevance;

public class CsvRelevanceRecorder implements RelevanceRecorder {

	private String outputDir;
	
	private FlatFileItemWriter<UserContentRelevance> itemWriter;
	
	public CsvRelevanceRecorder(String outputDir) {
		this.outputDir = outputDir;
	}
	
	@Override
	public void write(List<UserContentRelevance> recUserContentRelevance, ContentDataRecord rec) throws Exception {
		itemWriter.write(recUserContentRelevance);
	}

	private FlatFileItemWriter<UserContentRelevance> createItemWriter(ExecutionContext ctx) {
		
		FlatFileItemWriter<UserContentRelevance> csvWriter = new FlatFileItemWriter<>();
		
		DelimitedLineAggregator<UserContentRelevance> lineAggregator = new DelimitedLineAggregator<>();
		RelevanceInfoFieldExtractor fieldExtractor = new RelevanceInfoFieldExtractor();
		
		lineAggregator.setFieldExtractor(fieldExtractor);
		csvWriter.setLineAggregator(lineAggregator);
		
		String outFile = outputDir + File.separator + "content-bayes-" 
				+ ProcessContext.get().getTenantId() + "-"
				+ DateUtil.dateToLongDateString(ctx.getRunAsDate()) + "-" 
				+ Calendar.getInstance().getTimeInMillis() + ".csv";
		
		csvWriter.setResource(new FileSystemResource(outFile));
		
		csvWriter.setHeaderCallback(fieldExtractor);
		
		return csvWriter;
	}

	@Override
	public void open(ExecutionContext ctx) {
		this.itemWriter = createItemWriter(ctx);
		this.itemWriter.open(new org.springframework.batch.item.ExecutionContext());
	}

	@Override
	public void close() {
		this.itemWriter.close();
	}
	
	public static class RelevanceInfoFieldExtractor implements FieldExtractor<UserContentRelevance>, FlatFileHeaderCallback {

		private static class LogNode {
			
			private String name;
			private String[] states;

			public LogNode(String name, String[] states) {
				super();
				this.name = name;
				this.states = states;
			}

			@Override
			public String toString() {
				return "LogNode [name=" + name + "]";
			}
			
			
		}
		
		private static final LogNode[] NODE_LIST = { 
				new LogNode(ContentBayesNet.IS_NEW_CONTENT_NN, EBXNet.States.BOOLS), 
				new LogNode(ContentBayesNet.IS_RECENTLY_UPDT_CONTENT_NN, EBXNet.States.BOOLS),
				new LogNode(ContentBayesNet.IS_CONTENT_UPDT_AFTER_ACCESS_NN, EBXNet.States.BOOLS),
				new LogNode(ContentBayesNet.IS_RECENT_CONTENT_NN, EBXNet.States.BOOLS),
				new LogNode(ContentBayesNet.PEER_ACCESSED_CONTENT_NN,EBXNet.States.HIGH_LOW_STATES),
				new LogNode(ContentBayesNet.PEER_ACCESSED_CONTENT_TYPE_NN, EBXNet.States.HIGH_LOW_STATES),
				new LogNode(ContentBayesNet.IS_POPULAR_AMONG_PEERS_NN, EBXNet.States.BOOLS)
		};
		

		@Override
		public void writeHeader(Writer writer) throws IOException {
			
			StringBuilder nodeList = new StringBuilder();
			
			for (LogNode node : NODE_LIST) {
				for (String state : node.states) {
					nodeList.append(enclosedInQuotes(UserContentRelevance.nodeStateKey(node.name, state))).append(",");
				}
			}
			
			writer.write("\"User Id\",\"Content Type\",\"Content Identity\",\"Content Title\",\"Relevance\"," 
							+ nodeList.toString());
		}

		@Override
		public Object[] extract(UserContentRelevance item) {
			
			List<Object> propValues = new ArrayList<>(5 + NODE_LIST.length);
			
			propValues.add(enclosedInQuotes(item.getUserId()));
			propValues.add(enclosedInQuotes(item.getContainerLabel()));
			propValues.add(enclosedInQuotes(item.getContentIdentity()));
			propValues.add(enclosedInQuotes(item.getContentTitle()));
			propValues.add(enclosedInQuotes(item.getRelevance()));
			
			for (LogNode nodeName : NODE_LIST) {
				for (String state : nodeName.states) {
					NodeRelevance nodeRelevance = item.getNodeRelevance(nodeName.name, state);
					propValues.add(enclosedInQuotes(nodeRelevance != null ? nodeRelevance.getRelevance() : ""));
				}
			}
			
			return propValues.toArray();
		}
		
		private String enclosedInQuotes(Object obj) {
			return obj == null ? "\"\"" : ("\"" + String.valueOf(obj) + "\"");
		}
		
	}

}
