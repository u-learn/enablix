package com.enablix.app.content.ui.nav;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.services.util.DataViewUtil;

@Component
public class NavigationPathServiceImpl implements NavigationPathService {

	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public NavigableContent getNavPath(String containerQId, String contentIdentity) {

		NavigableContent navPath = null;
		
		String templateId = ProcessContext.get().getTemplateId();
		Object record = dataMgr.fetchDataJson(new FetchContentRequest(templateId, 
				containerQId, null, contentIdentity), DataViewUtil.allDataView());
		
		if (record instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapRecord = (Map<String, Object>) record;
			
			navPath = navContentBuilder.build(mapRecord, templateId, containerQId, labelResolver);
		}
		
		return navPath;
	}

}
