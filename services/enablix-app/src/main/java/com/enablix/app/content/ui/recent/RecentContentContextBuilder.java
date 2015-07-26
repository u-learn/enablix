package com.enablix.app.content.ui.recent;

import com.enablix.analytics.context.ContentRequest;

public interface RecentContentContextBuilder<R extends ContentRequest> {

	RecentContentContext build(R request);
	
}
