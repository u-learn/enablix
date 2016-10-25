package com.enablix.content.approval.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.state.change.model.StateChangeRecording;

@Document(collection = "ebx_content_approval")
public class ContentApproval extends StateChangeRecording<ContentDetail> {

}
