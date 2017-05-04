package com.enablix.app.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.mongo.dao.GenericDao;

@Component
public class OrderEntityService {

	@Autowired
	private GenericDao dao;
	
	public boolean swapOrder(SwapOrderRequest request) {
		updateOrderValue(request.getCollectionName(), request.getRecord1Identity(), request.getRecord1NewOrder());
		updateOrderValue(request.getCollectionName(), request.getRecord2Identity(), request.getRecord2NewOrder());
		return true;
	}

	private void updateOrderValue(String collectionName, String recordIdentity, int newOrder) {
		
		Update updateRecord = new Update();
		updateRecord.set(AppConstants.ORDER_ATTR_ID, newOrder);
		
		Criteria criteria = Criteria.where(ContentDataConstants.IDENTITY_KEY).is(recordIdentity);
		Query query = Query.query(criteria);
		
		dao.updateMulti(query, updateRecord, collectionName);
	}
	
}
