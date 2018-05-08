import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { UserTaskService } from '../user-task.service';
import { UserTask, UserTaskStep } from '../../model/user-task.model';
import { NavigationService } from '../../../app-routing/navigation.service';

import { AppEventService } from '../../app-event.service';
 
@Component({
  selector: 'ebx-user-task',
  templateUrl: './user-task.component.html',
  styleUrls: ['./user-task.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UserTaskComponent implements OnInit {

  userTasks: UserTask[];

  constructor(private userTaskService: UserTaskService,
    private navService: NavigationService,
    private appEventService: AppEventService) { }

  ngOnInit() {
    this.userTaskService.getPendingUserTasks().subscribe(
      (uts) => {
        this.userTasks = uts;
        if (uts) {
          uts.forEach((ut) => {
            if (ut.steps) {
              for (let step of ut.steps) {
                if (step.status != 'COMPLETE') {
                  ut.focusStep = step;
                  step.focus = true;
                  break;
                }
              }  
            }
          });
        }
      },
      (err) => {
        console.log("Error retrieving user tasks: ");
        console.log(err);
      }
    );
  }

  executeStep(ut: UserTask, step: UserTaskStep) {
    
    if (step.details && step.details.routePath) {
      
      this.userTaskService.updateTaskMilestone(ut.identity, step.stepId, 'link-visit')
          .subscribe((result: any) => {
            ut.steps = result.steps;
          });

      this.navService.goToRoute(step.details.routePath, []);

    } else if (step.details && step.details.action && step.details.action == 'open-add-asset') {
      
      this.userTaskService.updateTaskMilestone(ut.identity, step.stepId, 'open-add-asset')
          .subscribe((result: any) => {
            ut.steps = result.steps;
          });      

      this.appEventService.triggerOpenAddEvent();
    }
  }

  focusOnStep(ut: UserTask, step: UserTaskStep) {
    
    ut.steps.forEach((st) => {
      st.focus = false;
    });

    ut.focusStep = step;
    step.focus = true;
  }

  getProgressDesc(ut: UserTask) {
    let desc = ut.details && ut.details.progressLabel ? ut.details.progressLabel : "Progress";
    desc += " - " + this.getCompletedStepCount(ut) + "/" + ut.steps.length + " Steps Completed";
    return desc;
  }

  getCompletedStepCount(ut: UserTask) {
    
    let count = 0;
    
    ut.steps.forEach((step) => {
      if (step.status == 'COMPLETE') {
        count++;
      }
    });

    return count;
  }

  getCompletionPercent(ut: UserTask) {
    return ut.steps && ut.steps.length > 1 ? (this.getCompletedStepCount(ut)*100/ut.steps.length) : 0; 
  }

  getStepIcon(utStep: UserTaskStep) {
    
    let icon = "/assets/images/icons/note.svg"; //default icon
    
    if (utStep.status == 'COMPLETE') {
      if (utStep.details && utStep.details.doneIcon && utStep.details.doneIcon.length > 0) {
        icon = utStep.details.doneIcon;
      }
    } else {
      if (utStep.details && utStep.details.icon && utStep.details.icon.length > 0) {
        icon = utStep.details.icon;
      }
    }

    return icon;
  }

}
