
package com.enablix.core.mail.velocity.resolver;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.content.recent.RecentContentService;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;
import com.enablix.core.mongo.search.BoolFilter;
import com.enablix.core.mongo.search.DateFilter;
import com.enablix.core.mongo.search.SearchCriteria;
import com.enablix.data.view.DataView;

@Component
public class WeeklyDigestDataResolver implements VelocityTemplateInputResolver<WeeklyDigestVelocityInput> {

	@Autowired
	private RecentContentService recentService;
	
	//@Autowired
	//private ContentDataManager dataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Value("${recent.data.lookback.days:7}")
	private int recentDataLookbackDays;
	
	@Override
	public void work(WeeklyDigestVelocityInput weeklyDigestVelocityInput, DataView view) {

		String templateId = weeklyDigestVelocityInput.getTenant().getDefaultTemplateId();
		
		SearchCriteria recentDataCriteria = createRecentDataCriteria();
		List<NavigableContent> recentData = recentService.getRecentContentByCriteria(recentDataCriteria, view);
		weeklyDigestVelocityInput.setRecentList("RecentlyUpdated", recentData);
		
		ContentTemplate template = templateMgr.getTemplate(templateId);
		List<ContainerType> containers = template.getDataDefinition().getContainer();
		ArrayList<String> containerIds = new ArrayList<String>(containers.size());
		for (ContainerType containerType : containers) {
			containerIds.add(containerType.getId());
		}
		
		// TODO: correctly implement randomization in sidebar content
		HashMap<String, List<HashMap<String,Object>> > sidebarContent = new HashMap<>();
		//int selectedRandomInteger = (containerIds.size()==1)? 0 : selectRandomInteger(0, containerIds.size(), new Random());
		
			/*if(containerIds.indexOf("competitor") >= 0){				
				Object sideBarItem1 = dataMgr.fetchDataJson(new FetchContentRequest(templateId, containerIds.get(containerIds.indexOf("competitor")), null, null, new PageRequest(0, 5)));
				List<HashMap<String,Object>> sideBarContent1 = (List<HashMap<String,Object>>) ((PageImpl<?>) sideBarItem1).getContent();
				sidebarContent.put(containerIds.get(containerIds.indexOf("competitor")), sideBarContent1);				
			}
			
			if(containerIds.indexOf("solution") >= 0){
				Object sideBarItem2 = dataMgr.fetchDataJson(new FetchContentRequest(templateId, containerIds.get(containerIds.indexOf("solution")), null, null, new PageRequest(0, 5)));
				List<HashMap<String,Object>> sideBarContent2 = (List<HashMap<String,Object>>) ((PageImpl<?>) sideBarItem2).getContent();
				sidebarContent.put(containerIds.get(containerIds.indexOf("solution")), sideBarContent2);
			}*/
			
		weeklyDigestVelocityInput.setSideBarItems(sidebarContent);		
		//get solutions, competitors
		
	}
	
	private SearchCriteria createRecentDataCriteria() {
		
		Calendar lookbackLimitDate = Calendar.getInstance();
		lookbackLimitDate.add(Calendar.DAY_OF_YEAR, -recentDataLookbackDays);
		
		BoolFilter obsoleteFilter = new BoolFilter(
				ContentDataConstants.RECENT_DATA_OBSOLETE_ATTR, 
				Boolean.FALSE, ConditionOperator.EQ);
		
		DateFilter dateFilter = new DateFilter(ContentDataConstants.CREATED_AT_KEY, 
				lookbackLimitDate.getTime(), ConditionOperator.GTE);
		
		return new SearchCriteria(obsoleteFilter.and(dateFilter));
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof WeeklyDigestVelocityInput;
	}
	
/*	private static int selectRandomInteger(int aStart, int aEnd, Random aRandom) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		// get the range, casting to long to avoid overflow problems
		long range = (long) aEnd - (long) aStart + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * aRandom.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}*/

	@Override
	public float executionOrder() {
		return VelocityResolverExecOrder.WEEKLY_DIGEST_EXEC_ORDER;
	}
	
}
