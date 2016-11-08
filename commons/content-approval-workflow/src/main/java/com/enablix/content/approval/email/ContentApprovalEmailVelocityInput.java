
package com.enablix.content.approval.email;

import com.enablix.content.approval.model.ContentApproval;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.velocity.input.BaseVelocityInput;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.core.mail.velocity.input.LoggedInUserAware;
import com.enablix.core.mail.velocity.input.RecipientUserAware;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.state.change.model.ActionInput;

public class ContentApprovalEmailVelocityInput<I extends ActionInput> extends BaseVelocityInput implements LoggedInUserAware, RecipientUserAware, EnvPropertiesAware {

	private String url;
	private User loggedInUser;
	private String recipientUserId;
	private User recipientUser;
	private String actionName;
	private I actionInput;
	private ContentApproval contentRequest;
	private DisplayableContent contentData;
	
	public ContentApprovalEmailVelocityInput(String actionName, I actionInput, ContentApproval contentRequest) {
		this.actionInput = actionInput;
		this.actionName = actionName;
		this.contentRequest = contentRequest;
	}
	
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	@Override
	public User getLoggedInUser() {
		return loggedInUser;
	}

	@Override
	public String getRecipientUserId() {
		return recipientUserId;
	}

	@Override
	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}

	public User getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUserId(String recipientUserId) {
		this.recipientUserId = recipientUserId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public I getActionInput() {
		return actionInput;
	}

	public void setActionInput(I actionInput) {
		this.actionInput = actionInput;
	}

	public ContentApproval getContentRequest() {
		return contentRequest;
	}

	public void setContentRequest(ContentApproval contentRequest) {
		this.contentRequest = contentRequest;
	}

	public DisplayableContent getContentData() {
		return contentData;
	}

	public void setContentData(DisplayableContent contentData) {
		this.contentData = contentData;
	}

}
