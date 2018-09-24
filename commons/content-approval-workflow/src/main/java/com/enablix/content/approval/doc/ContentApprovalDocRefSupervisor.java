package com.enablix.content.approval.doc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocReferenceSupervisor;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class ContentApprovalDocRefSupervisor implements DocReferenceSupervisor {

	@Autowired
	private GenericDao genericDao;
	
	@Override
	public void updateDocMetadataAttr(DocumentMetadata docMetadata, String attrId, Object attrValue) {
		
		if (!StringUtil.isEmpty(docMetadata.getContentQId())) {
			
			// update in content collection as well
			String docQId = docMetadata.getContentQId();
		
			String docAttrId = "objectRef.data." + QIdUtil.getElementId(docQId);
			String docIdentityAttr = docAttrId + "." + ContentDataConstants.IDENTITY_KEY;
			String updateAttr = docAttrId + "." + attrId;
			
			Query query = new Query(Criteria.where(docIdentityAttr).is(docMetadata.getIdentity()));

			Update update = new Update();
			update.set(updateAttr, attrValue);

			genericDao.updateMulti(query, update, ContentApproval.class);
		}
	}
	
	@Override
	public boolean checkReferenceRecordExists(DocumentMetadata docMetadata) {

		boolean exists = false;
		
		String docIdentity = docMetadata.getIdentity();
		String docQId = docMetadata.getContentQId();
	
		if (!StringUtil.isEmpty(docQId)) {
			String docAttrId = "objectRef.data." + QIdUtil.getElementId(docQId);
			String docIdentityAttr = docAttrId + "." + ContentDataConstants.IDENTITY_KEY;
			
			StringFilter docIdentitFilter = new StringFilter(docIdentityAttr , docIdentity, ConditionOperator.EQ);
			Criteria criteria = docIdentitFilter.toPredicate(new Criteria());
	
			Long recordCnt = genericDao.countByCriteria(criteria, "ebx_content_approval", ContentApproval.class, MongoDataView.ALL_DATA);
	
			exists = recordCnt > 0;
		}
		
		return exists;
	}

}
