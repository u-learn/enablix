package com.enablix.app.content.kit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.service.CrudResponse;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.content.kit.ContentKit;
import com.enablix.core.domain.content.kit.ContentKitSummary;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.StringListFilter;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class ContentKitManagerImpl implements ContentKitManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentKitManagerImpl.class);

	private static final String NAME_ATTR = "name";
	private static final List<String> KIT_REF_FIELDS = new ArrayList<>();
	
	static {
		KIT_REF_FIELDS.add(ContentDataConstants.IDENTITY_KEY);
		KIT_REF_FIELDS.add(ContentDataConstants.CREATED_AT_KEY);
		KIT_REF_FIELDS.add(ContentDataConstants.CREATED_BY_KEY);
		KIT_REF_FIELDS.add(ContentDataConstants.CREATED_BY_NAME_KEY);
		KIT_REF_FIELDS.add(ContentDataConstants.MODIFIED_AT_KEY);
		KIT_REF_FIELDS.add(ContentDataConstants.MODIFIED_BY_KEY);
		KIT_REF_FIELDS.add(ContentDataConstants.MODIFIED_BY_NAME_KEY);
		KIT_REF_FIELDS.add(NAME_ATTR);
	}
	
	@Autowired
	private ContentKitCrudService crud;
	
	@Autowired
	private ContentCrudService contentCrud;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao genericDao;
	
	@Override
	public CrudResponse<ContentKit> saveOrUpdateKit(ContentKit kit) {
		return crud.saveOrUpdate(kit);
	}

	@Override
	public ContentKitBundle getContentKitBundle(String kitIdentity) {
		
		ContentKitBundle bundle = null;
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateWrapper templateWrapper = templateMgr.getTemplateWrapper(templateId);
		
		ContentKitDetail kitDetail = getContentKitDetail(kitIdentity);
		
		if (kitDetail != null) {
			
			bundle = new ContentKitBundle();
			bundle.setContentKitDetail(kitDetail);
			
			// set content record list
			List<ContentDataRecord> contentRecords = new ArrayList<>();
			bundle.setContentRecords(contentRecords);
			
			ContentKit contentKit = kitDetail.getContentKit(); 
			
			// group by containerQId so that we can reduce the number of DB queries
			Map<String, List<String>> containerIdentities = groupContentIdentitiesByContainerQId(contentKit);
			
			// iterate for each container type and fetch corresponding records
			for (Map.Entry<String, List<String>> entry : containerIdentities.entrySet()) {
			
				String containerQId = entry.getKey();
				String collectionName = templateWrapper.getCollectionName(containerQId);
				
				if (!StringUtil.isEmpty(collectionName)) {
					
					List<Map<String, Object>> dataRecords = contentCrud.findRecords(collectionName, entry.getValue());
					
					for (Map<String, Object> dataRec : dataRecords) {
						contentRecords.add(new ContentDataRecord(templateId, containerQId, dataRec));
					}
					
				} else {
					LOGGER.warn("Collection name not resolved for [{}] container", containerQId);
				}
			}
			
		}
		
		return bundle;
	}
	
	private Map<String, List<String>> groupContentIdentitiesByContainerQId(ContentKit contentKit) {
		
		Map<String, List<String>> containerIdentities = new LinkedHashMap<>();
		
		for (ContentDataRef ref : contentKit.getContentList()) {
		
			List<String> identities = containerIdentities.get(ref.getContainerQId());
			
			if (identities == null) {
				identities = new ArrayList<>();
				containerIdentities.put(ref.getContainerQId(), identities);
			}
			
			identities.add(ref.getInstanceIdentity());
		}
		
		return containerIdentities;
	}

	@Override
	public void deleteKit(String contentKitIdentity) {
		crud.getRepository().deleteByIdentity(contentKitIdentity);
	}

	@Override
	public ContentKit getContentKit(String contentKitIdentity) {
		return crud.getRepository().findByIdentity(contentKitIdentity);
	}

	@Override
	public ContentKitDetail getContentKitDetail(String contentKitIdentity) {
		
		ContentKitDetail kitDetail = null;
		
		ContentKit contentKit = crud.getRepository().findByIdentity(contentKitIdentity);
		
		if (contentKit != null) {
			
			kitDetail = new ContentKitDetail();
			kitDetail.setContentKit(contentKit);
			
			List<String> linkedKitIdentities = contentKit.getLinkedKits();
			if (CollectionUtil.isNotEmpty(linkedKitIdentities)) {
				kitDetail.setLinkedKits(getLinkedKitRefs(linkedKitIdentities));
			}
			
		} else {
			LOGGER.error("No content kit found for identity [{}]", contentKitIdentity);
		}
		
		return kitDetail;
	}
	
	private List<ContentKitSummary> getLinkedKitRefs(List<String> kitIdentities) {
		
		List<ContentKitSummary> linkedKitRefs = new ArrayList<>();
		
		StringListFilter identitiesFilter = new StringListFilter(
				ContentDataConstants.IDENTITY_KEY, kitIdentities, ConditionOperator.IN);
		
		List<ContentKit> linkedKitDetails = genericDao.findByCriteria(
				identitiesFilter.toPredicate(new Criteria()), ContentKit.class, KIT_REF_FIELDS);
		
		for (ContentKit kit : linkedKitDetails) {
			linkedKitRefs.add(kit.toSummary());
		}
		
		return linkedKitRefs;
	}
	
}
