package com.enablix.app.content.doc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class ContentDocReferenceSupervisor implements DocReferenceSupervisor {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao genericDao;
	
	@Override
	public void updateDocMetadataAttr(DocumentMetadata docMetadata, String attrId, Object attrValue) {
		
		if (!StringUtil.isEmpty(docMetadata.getContentQId())) {
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			// update in content collection as well
			String docQId = docMetadata.getContentQId();
			String parentQId = QIdUtil.getParentQId(docQId);
			String parentCollName = template.getCollectionName(parentQId);
			
			if (StringUtil.hasText(parentCollName)) {
			
				String docAttrId = QIdUtil.getElementId(docQId);
				String docIdentityAttr = docAttrId + "." + ContentDataConstants.IDENTITY_KEY;
				String updateAttr = docAttrId + "." + attrId;
				
				Query query = new Query(Criteria.where(docIdentityAttr).is(docMetadata.getIdentity()));
	
				Update update = new Update();
				update.set(updateAttr, attrValue);
	
				genericDao.updateMulti(query, update, parentCollName);
			}
		}
	}
	
	@Override
	public boolean checkReferenceRecordExists(DocumentMetadata docMetadata) {

		boolean exists = false;
		
		if (StringUtil.hasText(docMetadata.getContentQId())) {
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
	
			String docIdentity = docMetadata.getIdentity();
			String docQId = docMetadata.getContentQId();
			String parentQId = QIdUtil.getParentQId(docQId);
			String parentCollName = template.getCollectionName(parentQId);
			
			if (StringUtil.hasText(parentCollName)) {
			
				String docAttrId = QIdUtil.getElementId(docQId);
				String docIdentityAttr = docAttrId + "." + ContentDataConstants.IDENTITY_KEY;
				
				StringFilter docIdentitFilter = new StringFilter(docIdentityAttr , docIdentity, ConditionOperator.EQ);
				Criteria criteria = docIdentitFilter.toPredicate(new Criteria());
	
				Long recordCnt = genericDao.countByCriteria(criteria, parentCollName, Map.class, MongoDataView.ALL_DATA);
	
				exists = recordCnt > 0;
			}
		}
		
		return exists;
	}

}
