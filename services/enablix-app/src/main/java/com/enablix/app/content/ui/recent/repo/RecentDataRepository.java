package com.enablix.app.content.ui.recent.repo;

import java.util.Collection;

import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface RecentDataRepository extends BaseMongoRepository<RecentData> {

	@Query("{ $and : [{'scope.templateId' : ?0 }, "
			   + "{'scope.containerQId' : ?1 }, "
			   + "{'scope.contentIdentity' : ?2 } ]}")
	Collection<RecentData> findByTemplateIdAndContainerQIdAndContentIdentity(
		String templateId, String containerQId, String contentIdentity);

	@Query("{ $and : [{'scope.templateId' : ?0 }, "
			   + "{'scope.containerQId' : {$exists : false} }, "
			   + "{'scope.contentIdentity' : {$exists: false} } ]}")
	Collection<RecentData> findByTemplateId(String templateId);

	@Query("{ $and : [{'scope.templateId' : ?0 }, "
				   + "{'scope.containerQId' : ?1 }, "
				   + "{'scope.contentIdentity' : {$exists : false} } ]}")
	Collection<RecentData> findByTemplateIdAndContainerQId(String templateId, String containerQId);
	
}
