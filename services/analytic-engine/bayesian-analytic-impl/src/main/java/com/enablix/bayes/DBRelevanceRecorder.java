package com.enablix.bayes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.content.UserContentRelevance;
import com.enablix.core.mongo.MongoUtil;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.repo.UserContentRelevanceRepository;
import com.enablix.services.util.ContentDataUtil;

@Component
public class DBRelevanceRecorder implements RelevanceRecorder {

	@Autowired
	private UserContentRelevanceRepository repo;
	
	@Autowired
	private GenericDao dao;
	
	@Override
	public void write(List<UserContentRelevance> recUserContentRelevance, ContentDataRecord rec) throws Exception {
		updateOldRecords(recUserContentRelevance, rec);
		repo.save(recUserContentRelevance);
	}

	@Override
	public void close() {
		
	}

	@Override
	public void open(ExecutionContext ctx) {
		
	}
	
	private void updateOldRecords(List<UserContentRelevance> recUserContentRelevance, ContentDataRecord rec) {
		
		List<String> userIds = recUserContentRelevance.stream().map(UserContentRelevance::getUserId).collect(Collectors.toList());
		Criteria filter = Criteria.where("contentIdentity").is(ContentDataUtil.getRecordIdentity(rec.getRecord()))
								  .and("userId").in(userIds);
		
		MongoUtil.updateLastestFlagOfOldRecords(dao, UserContentRelevance.class, filter);
	}

}
