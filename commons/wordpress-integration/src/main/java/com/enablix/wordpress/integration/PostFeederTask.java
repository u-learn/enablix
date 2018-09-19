package com.enablix.wordpress.integration;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.api.Contexts;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest.Builder;
import com.enablix.analytics.info.detection.InfoDetector;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.tenant.TenantUtil;
import com.enablix.core.activity.audit.ActivityTrackingConstants;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;
import com.enablix.wordpress.model.WordpressInfo;

@Component
@PerTenantTask
public class PostFeederTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostFeederTask.class);
	
	@Autowired
	private WordpressService wpService;
	
	@Autowired
	private InfoDetector infoDetector;
	
	@Override
	public void run(TaskContext context) {
		
		Date lastRunDate = null;
		String tenantId = TenantUtil.getTenantId();
		
		Wordpress wp;
		
		try {
		
			wp = wpService.createClient();
			
			Object lastRun = context.getTaskParameter(tenantId, 
					WordpressConstants.WP_POST_FEEDER_TASK_LAST_RUN_PARAM);
			
			if (lastRun instanceof Date) {
				lastRunDate = (Date) lastRun;
			}
			
			if (lastRunDate == null) {
				Calendar today = Calendar.getInstance();
				today.add(Calendar.DAY_OF_MONTH, -1); // yesterday
				lastRunDate = DateUtil.getStartOfDay(today.getTime());
			}
			
			Date currentRunDate = Calendar.getInstance().getTime();
			
			LOGGER.debug("Looking for Wordpress post published after: {}", lastRunDate);
			
			WPIntegrationProperties wpProps = WPIntegrationProperties.getFromConfiguration();
			
			Builder<Post> srBuilder = SearchRequest.Builder.aSearchRequest(Post.class)
						 .withContext(Contexts.VIEW)
						 .withParam(WordpressConstants.WP_API_PARAM_PUBLISHED_AFTER, 
								 DateUtil.dateToUTCiso8601String(lastRunDate));
			
			addDefaultFilters(srBuilder, wpProps);
			
			SearchRequest<Post> searchRequest = srBuilder.withOrderAsc().build();
			
			setActivityTrackingContext();
			
			Boolean updateExisting = (Boolean) context.getTaskParameter(WordpressConstants.WP_POST_FEEDER_TASK_LAST_RUN_PARAM);
			WPInfoDetector wpInfoDetector = new WPInfoDetector(wp, updateExisting != null ? updateExisting.booleanValue() : false);
			wpService.processPosts(wp, wpInfoDetector, searchRequest);
			
			LOGGER.info("Total posts processed: " + wpInfoDetector.postCount);
			
			context.updateTaskParameter(tenantId, 
					WordpressConstants.WP_POST_FEEDER_TASK_LAST_RUN_PARAM, currentRunDate);

		} catch (WPConfigNotFoundException e) {
			
			LOGGER.info("Wordpress config missing for tenant [{}]: {}", tenantId, e.getMessage());
			
		} finally {
			ActivityTrackingContext.clear();
		}
		
	}

	private void addDefaultFilters(Builder<Post> srBuilder, WPIntegrationProperties wpProps) {
		if (CollectionUtil.isNotEmpty(wpProps.getTaskDefaultFilters())) {
			srBuilder.withParams(wpProps.getTaskDefaultFilters());
		}
	}

	private void setActivityTrackingContext() {
		
		String taskExecId = IdentityUtil.generateIdentity(this);
		
		Map<String, String> ctxParams = new HashMap<>();
		ctxParams.put(ActivityTrackingConstants.ACTIVITY_CHANNEL, ActivityChannel.Channel.SYSTEM.toString());
		ctxParams.put(ActivityTrackingConstants.CONTEXT_NAME, WordpressConstants.WP_POST_IMPORT_ACTVY_CTX);
		ctxParams.put(ActivityTrackingConstants.CONTEXT_ID, taskExecId);
		
		ActivityTrackingContext.initialize(ctxParams);
	}

	@Override
	public String taskId() {
		return "wp-post-feeder-task";
	}

	private class WPInfoDetector implements WPPostProcessor {

		private Wordpress wp;
		
		private int postCount;
		
		private boolean updateExisting;
		
		private WPInfoDetector(Wordpress wp, boolean updateExisting) {
			super();
			this.wp = wp;
			this.updateExisting = updateExisting;
		}


		@Override
		public void process(Post post) {
			WordpressInfo wpInfo = new WordpressInfo(post, wpService.getPostTagsAndCategories(wp, post));
			infoDetector.analyseAndSaveContentRecord(wpInfo, this.updateExisting);
			postCount++;
		}
		
	}
	
}
