package com.enablix.wordpress.info.detection;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.wordpress.integration.WPIntegrationProperties;
import com.enablix.wordpress.model.WordpressInfo;

public class WPInfoAlreadyExistsFilter implements MessageSelector {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao genericDao;
	
	@Override
	public boolean accept(Message<?> message) {
		
		Object payload = message.getPayload();
		
		if (payload instanceof InfoDetectionContext) {
		
			InfoDetectionContext ctx = (InfoDetectionContext) payload;
			Information info = ctx.getInformation();
			
			if (info instanceof WordpressInfo) {
			
				WordpressInfo wpInfo = (WordpressInfo) info;
				
				Collection<TypeOpinion> typeOpinions = ctx.getAssessment().getTypeOpinions();
				
				if (CollectionUtil.isNotEmpty(typeOpinions)) {
					
					WPIntegrationProperties wpIntProps = null;
					InfoDetectionConfiguration config = ctx.getInfoDetectionConfig();
					
					if (config instanceof WPIntegrationProperties) {
						wpIntProps = (WPIntegrationProperties) config;
					} else {
						wpIntProps = WPIntegrationProperties.getFromConfiguration();
					}
					
					if (wpIntProps != null) {
					
						TemplateFacade template = 
								templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
						
						for (TypeOpinion typeOp : typeOpinions) {
							if (checkLatestInfoExists(ctx, wpInfo, wpIntProps, typeOp, template)) {
								return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean checkLatestInfoExists(InfoDetectionContext ctx, WordpressInfo wpInfo, 
			WPIntegrationProperties wpIntProps, TypeOpinion typeOp, TemplateFacade template) {
		
		String containerQId = typeOp.getContainerQId();
		String collectionName = template.getCollectionName(containerQId);
		
		String matchAttrId = wpIntProps.getContQIdToSlugMatchAttrId().get(containerQId);
		
		if (StringUtil.hasText(matchAttrId)) {
			
			Criteria criteria = Criteria.where(matchAttrId).regex(
					Pattern.compile(wpInfo.getPost().getSlug(), Pattern.CASE_INSENSITIVE));
			
			List<Map> records = genericDao.findByCriteria(criteria, collectionName, Map.class, MongoDataView.ALL_DATA);
			
			if (CollectionUtil.isNotEmpty(records)) {
				
				Map record = records.get(0);
				typeOp.setExistingRecord(record);
				
				// the record exists. Check if update is required.
				return !ctx.isUpdateExistingRecord();
			}
			
		}
		
		return false;
	}

}
