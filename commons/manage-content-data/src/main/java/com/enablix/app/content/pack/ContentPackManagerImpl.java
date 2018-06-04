package com.enablix.app.content.pack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enablix.app.content.pack.repo.ContentPackRepository;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentPackDTO;
import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.data.view.DataView;

@Component
public class ContentPackManagerImpl implements ContentPackManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentPackManagerImpl.class);
	
	@Autowired
	private ContentPackDataResolverFactory resolverFactory;
	
	@Autowired
	private ContentPackRepository packRepo;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ContentPackDTO getContentPack(String packIdentity, DataView dataView, int pageNo, int pageSize) {

		ContentPackDTO response = null;
		
		ContentPack pack = packRepo.findByIdentity(packIdentity);
		
		if (pack != null) {
			
			ContentPackDataResolver dataResolver = resolverFactory.getResolver(pack.getType());
		
			if (dataResolver == null) {
				
				String errMessage = "No content pack resolver found for [" + pack.getType() + "]";
				
				LOGGER.error(errMessage);
				throw new IllegalStateException(errMessage);
			}
			
			Page<ContentDataRecord> dataPage = dataResolver.getData(pack, dataView, pageNo, pageSize);
			
			response = new ContentPackDTO();
			response.setPack(pack);
			response.setRecords(dataPage);
		}
		
		return response;
	}

}
