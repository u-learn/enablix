package com.enablix.es.config;

import static org.apache.commons.lang.StringUtils.split;
import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lease.Releasable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({ Client.class, ElasticsearchTemplate.class, 
	TransportClientFactoryBean.class, NodeClientFactoryBean.class })
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticSearchAutoConfigurer implements DisposableBean {
	
	private static final String COMMA = ",";
	private static final String COLON = ":";
	
	private static Logger logger = LoggerFactory.getLogger(ElasticSearchAutoConfigurer.class);

	private Boolean clientTransportSniff = true;
	private Boolean clientIgnoreClusterName = Boolean.FALSE;
	private String clientPingTimeout = "5s";
	private String clientNodesSamplerInterval = "5s";

	@Autowired
	private ElasticsearchProperties properties;

	private Releasable releasable;

	@Bean
	public Client elasticsearchClient() {
		try {
			return createClient();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	private Client createClient() throws Exception {
		if (StringUtils.hasLength(this.properties.getClusterNodes())) {
			return createTransportClient();
		}
		return createNodeClient();
	}

	private Client createNodeClient() throws Exception {
		Builder settings = Settings.builder().put(
				"http.enabled", String.valueOf(false));
		Node node = new NodeBuilder().settings(settings)
				.clusterName(this.properties.getClusterName()).local(true).node();
		this.releasable = node;
		return node.client();
	}

	private Client createTransportClient() throws Exception {
		
		Settings settings = Settings.builder()
				.put("cluster.name", properties.getClusterName())
				.put("client.transport.sniff", clientTransportSniff)
				.put("client.transport.ignore_cluster_name", clientIgnoreClusterName)
				.put("client.transport.ping_timeout", clientPingTimeout)
				.put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval)
				.build();
		
		TransportClient client = TransportClient.builder().settings(settings)
				.build();
		
		String clusterNodes = properties.getClusterNodes();
		
		Assert.hasText(clusterNodes, "[Assertion failed] clusterNodes settings missing.");
		
		for (String clusterNode : split(clusterNodes, COMMA)) {
		
			String hostName = substringBefore(clusterNode, COLON);
			String port = substringAfter(clusterNode, COLON);
			
			Assert.hasText(hostName, "[Assertion failed] missing host name in 'clusterNodes'");
			Assert.hasText(port, "[Assertion failed] missing port in 'clusterNodes'");
			
			logger.info("adding transport node : " + clusterNode);
			client.addTransportAddress(new InetSocketTransportAddress(
					InetAddress.getByName(hostName), Integer.valueOf(port)));
		}
		
		this.releasable = client;
		return client;
	}
	
	@Override
	public void destroy() throws Exception {
		if (this.releasable != null) {
			try {
				if (logger.isInfoEnabled()) {
					logger.info("Closing Elasticsearch client");
				}
				try {
					this.releasable.close();
				}
				catch (NoSuchMethodError ex) {
					// Earlier versions of Elasticsearch had a different method name
					ReflectionUtils.invokeMethod(
							ReflectionUtils.findMethod(Releasable.class, "release"),
							this.releasable);
				}
			}
			catch (final Exception ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Error closing Elasticsearch client: ", ex);
				}
			}
		}
	}
	
}
