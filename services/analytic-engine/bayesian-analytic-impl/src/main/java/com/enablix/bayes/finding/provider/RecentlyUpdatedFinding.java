package com.enablix.bayes.finding.provider;

import java.util.Calendar;
import java.util.Date;

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
public class RecentlyUpdatedFinding implements NodeFindingProvider {

	@Value("${bayes.recently.updated.finding.lookback:2}")
	private int lookback;
	
	@Override
	public String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec) {
		
		Calendar lookbackDate = Calendar.getInstance();
		
		lookbackDate.setTime(ctx.getRunAsDate());
		lookbackDate.add(Calendar.DAY_OF_YEAR, -lookback);
		
		String finding = null;
		
		Date contentCreatedAt = ContentDataUtil.getContentModifiedAt(dataRec.getRecord());
		
		if (contentCreatedAt != null) {
			
			Calendar createdAtCal = Calendar.getInstance();
			createdAtCal.setTime(contentCreatedAt);
			
			finding = lookbackDate.before(createdAtCal) ? EBXNet.TRUE : EBXNet.FALSE;
		}
		
		return finding;
	}

	@Override
	public String nodeName() {
		return ContentBayesNet.IS_RECENTLY_UPDT_CONTENT_NN;
	}

	@Override
	public void init(InitializationContext ctx) {

		
	}

}
