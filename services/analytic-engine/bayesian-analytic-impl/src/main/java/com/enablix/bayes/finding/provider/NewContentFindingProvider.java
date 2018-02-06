package com.enablix.bayes.finding.provider;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.bayes.EBXNet;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.services.util.ContentDataUtil;

@Component
public class NewContentFindingProvider implements NodeFindingProvider {

	private static Logger LOGGER = LoggerFactory.getLogger(NewContentFindingProvider.class);
	
	@Value("${bayes.new.content.finding.lookback:2}")
	private int lookback;
	
	@Override
	public String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec) {
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(ctx.getRunAsDate());
		cal.add(Calendar.DAY_OF_YEAR, -lookback);
		
		String finding = null;
		
		Date contentCreatedAt = ContentDataUtil.getContentCreatedAt(dataRec.getRecord());
		if (contentCreatedAt != null) {
			
			Calendar createdAtCal = Calendar.getInstance();
			createdAtCal.setTime(contentCreatedAt);
			
			LOGGER.debug("Comparing dates: {} v/s {} - {}", contentCreatedAt, cal.getTime(), cal.before(createdAtCal));
			finding = cal.before(createdAtCal) ? EBXNet.TRUE : EBXNet.FALSE;
		}
		
		return finding;
	}

	@Override
	public String nodeName() {
		return ContentBayesNet.IS_NEW_CONTENT_NN;
	}

	@Override
	public void init(InitializationContext ctx) {

		
	}

}
