package com.enablix.app.content.ui.nav;

import com.enablix.app.content.ui.NavigableContent;

public interface NavigationPathService {

	NavigableContent getNavPath(String containerQId, String contentIdentity);
	
}
