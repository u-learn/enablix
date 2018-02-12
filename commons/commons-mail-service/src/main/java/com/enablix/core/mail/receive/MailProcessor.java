package com.enablix.core.mail.receive;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface MailProcessor {

	void process(Message msg) throws MessagingException, IOException;

}
