package com.enablix.app.content.recent.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.domain.recent.RecentData.UpdateType;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface RecentDataRepository extends BaseMongoRepository<RecentData> {

	@Query("{ $and : [{'scope.templateId' : ?0 }, "
			   + "{'scope.containerQId' : ?1 }, "
			   + "{'scope.contentIdentity' : ?2 } ]}")
	Page<RecentData> findByTemplateIdAndContainerQIdAndContentIdentity(
		String templateId, String containerQId, String contentIdentity, Pageable sort);

	@Query("{ $and : [{'scope.templateId' : ?0 }, "
			   + "{'scope.containerQId' : {$exists : false} }, "
			   + "{'scope.contentIdentity' : {$exists: false} } ]}")
	Page<RecentData> findByTemplateId(String templateId, Pageable sort);

	@Query("{ $and : [{'scope.templateId' : ?0 }, "
				   + "{'scope.containerQId' : ?1 }, "
				   + "{'scope.contentIdentity' : {$exists : false} } ]}")
	Page<RecentData> findByTemplateIdAndContainerQId(String templateId, String containerQId, Pageable sort);
	
	// Since identity is unique across collection, we delete by identity
	Long deleteByDataInstanceIdentity(String identity);
	
	RecentData findByDataInstanceIdentityAndUpdateType(String identity, UpdateType updateType);
	
	List<RecentData> findByDataInstanceIdentity(String identity);
	
}
