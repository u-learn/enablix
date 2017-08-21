package com.enablix.app.mail.generic;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayableContentService;
import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;

@Component
public class SelectedContentEmailInputBuilder extends AbstractEmailVelocityInputBuilder<SelectedContentMailInput> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectedContentEmailInputBuilder.class);
	
	@Autowired
	private DisplayableContentService dcService;
	
	@Override
	public String mailType() {
		return GenericMailConstants.MAIL_TYPE_SELECTED_CONTENT;
	}

	@Override
	protected SelectedContentMailInput createInputInstance() {
		return new SelectedContentMailInput();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SelectedContentMailInput buildInput(SelectedContentMailInput input, EmailRequest request,
			DataView dataView) {
		
		Object contentRef = request.getInputData().get("content");
		
		if (contentRef != null && contentRef instanceof Map) {
			
			Map<String, List<String>> contentRefMap = (Map<String, List<String>>) contentRef;
			
			for (Map.Entry<String, List<String>> contentRefEntry : contentRefMap.entrySet()) {
			
				String contentQId = contentRefEntry.getKey();
				List<DisplayableContent> dcList = dcService.getDisplayableContent(
						contentQId, contentRefEntry.getValue(), dataView);
				
				input.addContent(contentQId, dcList);
			}
			
		} else {
			LOGGER.error("Missing or invalid type input [content]");
			throw new IllegalArgumentException("Missing or invalid type [content] input");
		}
		
		return input;
	}
	
	@Override
	public void processInputForRecipient(Recipient recipient, SelectedContentMailInput input, DataView dataView) {
		
		super.processInputForRecipient(recipient, input, dataView);
		
		for (Map.Entry<String, List<DisplayableContent>> dcEntry : input.getContent().entrySet()) {
			dcEntry.getValue().forEach((dc) -> dcService.postProcessDisplayableContent(dc, recipient.getEmailId()));
		}
		
	}

}
