package com.enablix.core.mail.service;

import java.util.List;

public interface MailContentProcessorFactory {

	List<MailContentProcessor> getProcessors(String mailScenarioName);
	
}
