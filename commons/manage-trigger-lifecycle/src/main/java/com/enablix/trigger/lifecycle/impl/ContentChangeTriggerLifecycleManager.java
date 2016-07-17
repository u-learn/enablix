package com.enablix.trigger.lifecycle.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ContentTriggerDefType;
import com.enablix.core.commons.xsdtopojo.LifecycleType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;
import com.enablix.core.domain.trigger.TriggerLifecycleRule;
import com.enablix.trigger.lifecycle.LifecycleCheckpointBuilder;
import com.enablix.trigger.lifecycle.TriggerLifecycleManager;
import com.enablix.trigger.lifecycle.repo.LifecycleCheckpointRepository;
import com.enablix.trigger.lifecycle.rule.repo.TriggerLifecycleRuleRepository;

@Component
public class ContentChangeTriggerLifecycleManager implements TriggerLifecycleManager<ContentChange> {

	@Autowired
	private TriggerLifecycleRuleRepository lifecycleRuleRepo;
	
	@Autowired
	private ContentChangeCheckpointExecutor checkpointExecutor;
	
	@Autowired
	private LifecycleCheckpointBuilder checkpointBuilder;
	
	@Autowired
	private LifecycleCheckpointRepository checkpointRepo;
	
	@Override
	public void startLifecycle(ContentChange trigger) {
		
		TriggerLifecycleRule lifecycleDef = 
				lifecycleRuleRepo.findByContentTriggerRuleTypeAndContentTriggerRuleCandidateContainersContainerQId(
						trigger.getTriggerType(), trigger.getTriggerItem().getContainerQId());
		
		if (lifecycleDef != null) {
			
			ContentTriggerDefType contentTriggerLifecycleRule = lifecycleDef.getContentTriggerRule();
			LifecycleType triggerLifecycleDef = contentTriggerLifecycleRule.getLifecycle();
			
			String triggerLifecycleId = IdentityUtil.generateIdentity(null);
			
			List<LifecycleCheckpoint<ContentChange>> checkpoints = new ArrayList<>();
			
			// build checkpoints
			for (CheckpointType checkpointDef : triggerLifecycleDef.getCheckpoint()) {
			
				LifecycleCheckpoint<ContentChange> lifecycleCheckpoint = checkpointBuilder.build(
						contentTriggerLifecycleRule, checkpointDef, trigger, triggerLifecycleId);
				checkpoints.add(lifecycleCheckpoint);
			}
			
			// save all checkpoints
			checkpoints = checkpointRepo.save(checkpoints);
			
			// execute checkpoints
			for (LifecycleCheckpoint<ContentChange> lifecycleCheckpoint : checkpoints) {
				checkpointExecutor.execute(lifecycleCheckpoint);
			}
			
		}
		
	}

	@Override
	public boolean canHandle(Trigger t) {
		return t instanceof ContentChange;
	}

}
