package com.enablix.trigger.lifecycle.action.email;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.DisableExecutionOnType;
import com.enablix.core.commons.xsdtopojo.LaterExecutionTimeType;
import com.enablix.core.commons.xsdtopojo.ReevaluatePendingCheckpointType;
import com.enablix.core.commons.xsdtopojo.ResetExecutionOnType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.LifecycleCheckpoint.ExecutionStatus;
import com.enablix.trigger.lifecycle.CheckpointExecTimeInterpreterFactory;
import com.enablix.trigger.lifecycle.CheckpointExecutionTimeInterpreter;
import com.enablix.trigger.lifecycle.action.CheckpointAction;
import com.enablix.trigger.lifecycle.repo.LifecycleCheckpointRepository;

@Component
public class ReevaluateCheckpointAction implements CheckpointAction<ContentChange, ReevaluatePendingCheckpointType> {

	@Autowired
	private LifecycleCheckpointRepository checkpointRepo;
	
	@Autowired
	private TriggerExecChangeOnEntityUpdateEvaluator onEntityUpdateEvaluator;
	
	@Autowired
	private CheckpointExecTimeInterpreterFactory execTimeInterpreterFactory;
	
	@Override
	public void run(LifecycleCheckpoint<ContentChange> checkpoint, TemplateFacade template,
			ReevaluatePendingCheckpointType actionType) {

		ContentChange currentTrigger = checkpoint.getTrigger();
		String instanceIdentity = currentTrigger.getTriggerItem().getInstanceIdentity();
		
		List<LifecycleCheckpoint<?>> pendingCheckpoints = 
				checkpointRepo.findByStatusAndTriggerItem(ExecutionStatus.PENDING, instanceIdentity);
		
		if (CollectionUtil.isNotEmpty(pendingCheckpoints)) {
			
			for (LifecycleCheckpoint<?> pendingCheckpt : pendingCheckpoints) {
			
				CheckpointType checkpointDef = pendingCheckpt.getCheckpointDefinition();
				
				LaterExecutionTimeType laterExecDef = checkpointDef.getExecutionTime().getAfter();
	
				if (laterExecDef != null) {
					
					pendingCheckpt = checkAndReset(pendingCheckpt, currentTrigger, 
										laterExecDef.getResetOn(), template);
				}
				
				DisableExecutionOnType disableOn = checkpointDef.getExecutionTime().getDisableOn();

				if (disableOn != null) {
					pendingCheckpt = checkAndDisable(pendingCheckpt, currentTrigger, 
							disableOn, template); 
				}
				
			}
		}
		
	}


	private LifecycleCheckpoint<?> checkAndReset(LifecycleCheckpoint<?> pendingCheckpt, 
			ContentChange trigger, ResetExecutionOnType resetOn, TemplateFacade template) {
		
		if (resetOn != null) {
			
			boolean reset = onEntityUpdateEvaluator.evaluateChangeCondition(
								trigger, resetOn.getTriggerEntityUpdate(), template);
			
			if (reset) {
				
				CheckpointType checkpointDef = pendingCheckpt.getCheckpointDefinition();
				
				CheckpointExecutionTimeInterpreter execTimeInterpreter = 
					execTimeInterpreterFactory.getInterpreter(checkpointDef);
				
				Date execTime = execTimeInterpreter.getCheckpointExecutionTime(checkpointDef, trigger);
				pendingCheckpt.setScheduledExecDate(execTime);
				pendingCheckpt = checkpointRepo.save(pendingCheckpt);
			}
		}
		
		return pendingCheckpt;
	}


	private LifecycleCheckpoint<?> checkAndDisable(LifecycleCheckpoint<?> checkpoint, 
			ContentChange trigger, DisableExecutionOnType disableOn, TemplateFacade template) {
		
		if (disableOn != null) {
			
			boolean disable = onEntityUpdateEvaluator.evaluateChangeCondition(
								trigger, disableOn.getTriggerEntityUpdate(), template);
			
			if (disable) {
				checkpoint.setStatus(ExecutionStatus.DISABLED);
				checkpoint = checkpointRepo.save(checkpoint);
			}
		}
		
		return checkpoint;
	}
	

	@Override
	public boolean canHandle(ActionType action) {
		return action instanceof ReevaluatePendingCheckpointType;
	}

}
