package com.enablix.bayes.finding.provider;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.bayes.EBXNet;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.audit.repo.ActivityAuditRepository;
import com.enablix.services.util.ContentDataUtil;

@Component
public class ContentUpdtAfterAccessFinding implements NodeFindingProvider {

	@Value("${bayes.recently.updated.finding.lookback:2}")
	private int recentLookback;
	
	@Value("${bayes.access.after.update.finding.lookback:2}")
	private int lookback;
	
	@Autowired
	private ActivityAuditRepository auditRepo;
	
	@Override
	public String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec) {
		
		Calendar recentLookbackDate = Calendar.getInstance();
		
		recentLookbackDate.setTime(ctx.getRunAsDate());
		recentLookbackDate.add(Calendar.DAY_OF_YEAR, -recentLookback);
		
		String finding = null;
		
		Date contentModifiedAt = ContentDataUtil.getContentModifiedAt(dataRec.getRecord());
		
		if (contentModifiedAt != null) {
			
			Calendar modifiedAtCal = Calendar.getInstance();
			modifiedAtCal.setTime(contentModifiedAt);
			
			if (recentLookbackDate.before(modifiedAtCal)) {
				
				// check if user access if before it was updated
				Calendar accessLookbackDate = Calendar.getInstance();
				accessLookbackDate.setTime(contentModifiedAt);
				accessLookbackDate.add(Calendar.DAY_OF_YEAR, -lookback);
				
				String recIdentity = ContentDataUtil.getRecordIdentity(dataRec.getRecord());

				Long count = auditRepo.countContentAccessByUserBetweenDates(
						recIdentity, user.getEmail(), 
						accessLookbackDate.getTime(), recentLookbackDate.getTime()
						);
				
				finding = (count != null && count > 0) ? EBXNet.States.TRUE : EBXNet.States.FALSE;
				
			} else {
				finding = EBXNet.States.FALSE;
			}
		}
		
		return finding;
	}

	@Override
	public String nodeName() {
		return ContentBayesNet.IS_CONTENT_UPDT_AFTER_ACCESS_NN;
	}

	@Override
	public void init(InitializationContext ctx) {

		
	}

}
