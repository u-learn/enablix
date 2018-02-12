package com.enablix.mail.info.task;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.InfoDetector;
import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.tenant.TenantUtil;
import com.enablix.core.activity.audit.ActivityTrackingConstants;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mail.receive.MailProcessor;
import com.enablix.core.mail.receive.MailReader;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.mail.info.MailInfo;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class MailFeederTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailFeederTask.class);
	
	@Autowired
	private MailReader mailReader;
	
	@Autowired
	private InfoDetector infoDetector;
	
	@Override
	public void run(TaskContext context) {
		
		Date lastProcessedMail = null;
		String tenantId = TenantUtil.getTenantId();
		
		try {
		
			Object lastMailDate = context.getTaskParameter(tenantId, 
					MailConstants.MAIL_POST_FEEDER_TASK_LAST_MAIL_PARAM);
			
			if (lastMailDate instanceof Date) {
				lastProcessedMail = (Date) lastMailDate;
			}
			
			if (lastMailDate == null) {
				Calendar today = Calendar.getInstance();
				today.add(Calendar.DAY_OF_MONTH, -1); // yesterday
				lastProcessedMail = DateUtil.getStartOfDay(today.getTime());
			}
			
			Configuration config = ConfigurationUtil.getConfig(MailConstants.CONTENT_FEEDER_INBOX_CONFIG_KEY);
			
			if (config != null) {

				Map<String, Object> configProps = config.getConfig();
				configProps = tranformMailProperties(configProps);
				Object from = configProps.get(MailConstants.MAIL_FROM_PROP);
				
				if (from != null) {
					
					String mailFrom = (String) from;
					String mailboxUser = (String) configProps.get(MailConstants.MAIL_USER_PROP);
					
					Calendar rcvdAfter = Calendar.getInstance();
					rcvdAfter.setTime(lastProcessedMail);
					rcvdAfter.add(Calendar.SECOND, 1);
					
					LOGGER.debug("Looking for Mails received after: {}, from: {}", rcvdAfter.getTime(), mailFrom);
					
					SearchTerm fromTerm = new FromTerm(new InternetAddress(mailFrom));
					ReceivedDateTerm rcvdAfterTerm = new ReceivedDateTerm(ComparisonTerm.GT, rcvdAfter.getTime());
					SearchTerm searchTerm = new AndTerm(fromTerm, rcvdAfterTerm);
					
					setActivityTrackingContext();
					
					Properties mailProperties = new Properties();
					mailProperties.putAll(configProps);
					
					MailInfoProcessor msgProcessor = new MailInfoProcessor(mailboxUser);
					mailReader.readMails(mailProperties, searchTerm, msgProcessor);
					
					Date lastReceivedMailDate = msgProcessor.getLastReceivedMailDate();
					
					if (lastReceivedMailDate != null) {
						context.updateTaskParameter(tenantId, 
							MailConstants.MAIL_POST_FEEDER_TASK_LAST_MAIL_PARAM, lastReceivedMailDate);
					}
					
					LOGGER.info("{} mails processed", msgProcessor.processedCount);
				}
				
			} else {
				LOGGER.info("Mail feeder config missing for tenant [{}]: {}", tenantId);
			}

		} catch (MessagingException | IOException e ) {
			
			LOGGER.error("Error reading mail feeder", e);
			
		} finally {
			ActivityTrackingContext.clear();
		}
		
	}

	private Map<String, Object> tranformMailProperties(Map<String, Object> configProps) {
		Map<String, Object> txProps = new HashMap<>();
		for (Map.Entry<String, Object> entry : configProps.entrySet()) {
			txProps.put(entry.getKey().replace('_', '.'), entry.getValue());
		}
		return txProps;
	}

	private void setActivityTrackingContext() {
		
		String taskExecId = IdentityUtil.generateIdentity(this);
		
		Map<String, String> ctxParams = new HashMap<>();
		ctxParams.put(ActivityTrackingConstants.ACTIVITY_CHANNEL, ActivityChannel.Channel.SYSTEM.toString());
		ctxParams.put(ActivityTrackingConstants.CONTEXT_NAME, MailConstants.MAIL_IMPORT_ACTVY_CTX);
		ctxParams.put(ActivityTrackingConstants.CONTEXT_ID, taskExecId);
		
		ActivityTrackingContext.initialize(ctxParams);
	}

	@Override
	public String taskId() {
		return "mail-feeder-task";
	}
	
	private class MailInfoProcessor implements MailProcessor {

		private Date lastReceivedMailDate;
		
		private String mailboxUser;
		
		private int processedCount;
		
		public MailInfoProcessor(String mailboxUser) {
			this.mailboxUser = mailboxUser;
			this.processedCount = 0;
		}
		
		@Override
		public void process(Message mail) throws MessagingException, IOException {
			
			MailInfo mailInfo = MailInfo.build(mail, mailboxUser);
			infoDetector.analyseAndSaveContentRecord(mailInfo);
			
			if (lastReceivedMailDate == null) {
				lastReceivedMailDate = mail.getReceivedDate();
			} else if (mail.getReceivedDate().after(lastReceivedMailDate)) {
				lastReceivedMailDate = mail.getReceivedDate();
			}
			
			processedCount++;
		}

		public Date getLastReceivedMailDate() {
			return lastReceivedMailDate;
		}
		
	}
	
}
