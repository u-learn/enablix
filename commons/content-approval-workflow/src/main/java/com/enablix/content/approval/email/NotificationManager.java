package com.enablix.content.approval.email;

public interface NotificationManager {

	void notifyNewRequest(NotificationPayload payload);
	
	void notifyApproval(NotificationPayload payload);
	
	void notifyRejection(NotificationPayload payload);
	
}
