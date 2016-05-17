
package com.enablix.core.mail.velocity.resolver;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.recent.RecentContentService;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;

@Component
public class WeeklyDigestDataResolver implements VelocityTemplateInputResolver<WeeklyDigestVelocityInput> {

	@Autowired
	private RecentContentService recentService;
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void work(WeeklyDigestVelocityInput weeklyDigestVelocityInput) {
		String tenantId = weeklyDigestVelocityInput.getTenant().getTenantId();
		String templateId = weeklyDigestVelocityInput.getTenant().getDefaultTemplateId();
		
		WebContentRequest request = new WebContentRequest();
		List<NavigableContent> recentData = recentService.getRecentContent(request);
		weeklyDigestVelocityInput.setRecentList("RecentlyUpdated", recentData);
		
		ContentTemplate template = templateMgr.getTemplate(templateId);
		List<ContainerType> containers = template.getDataDefinition().getContainer();
		ArrayList<String> containerIds = new ArrayList<String>(containers.size());
		for (ContainerType containerType : containers) {
			containerIds.add(containerType.getId());
		}
		
		HashMap<String, List<HashMap<String,Object>> > sidebarContent = new HashMap<>();
		//int selectedRandomInteger = (containerIds.size()==1)? 0 : selectRandomInteger(0, containerIds.size(), new Random());
		
			if(containerIds.indexOf("competitor") >= 0){				
				Object sideBarItem1 = dataMgr.fetchDataJson(new FetchContentRequest(templateId, containerIds.get(containerIds.indexOf("competitor")), null, null, new PageRequest(0, 5)));
				List<HashMap<String,Object>> sideBarContent1 = (List<HashMap<String,Object>>) ((PageImpl<?>) sideBarItem1).getContent();
				sidebarContent.put(containerIds.get(containerIds.indexOf("competitor")), sideBarContent1);				
			}
			
			if(containerIds.indexOf("solution") >= 0){
				Object sideBarItem2 = dataMgr.fetchDataJson(new FetchContentRequest(templateId, containerIds.get(containerIds.indexOf("solution")), null, null, new PageRequest(0, 5)));
				List<HashMap<String,Object>> sideBarContent2 = (List<HashMap<String,Object>>) ((PageImpl<?>) sideBarItem2).getContent();
				sidebarContent.put(containerIds.get(containerIds.indexOf("solution")), sideBarContent2);
			}
			
		weeklyDigestVelocityInput.setSideBarItems(sidebarContent);		
		//get solutions, competitors
		
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof WeeklyDigestVelocityInput;
	}
	
	private static int selectRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	  }
	  
	
	
}
