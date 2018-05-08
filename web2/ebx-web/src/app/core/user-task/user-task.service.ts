import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../api-url.service';
import { UserTask, UserTaskStep } from '../model/user-task.model';

@Injectable()
export class UserTaskService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  getPendingUserTasks() : Observable<UserTask[]> {
    let apiUrl = this.apiUrlService.getPendingUserTasksUrl();
    return this.http.get<UserTask[]>(apiUrl);
  }

  updateTaskMilestone(taskIdentity: string, stepId: string, milestone: string) {
    
    let data = {
      taskIdentity: taskIdentity,
      stepId: stepId,
      milestone: milestone
    }

    let apiUrl = this.apiUrlService.postUserTaskMilestoneUpdateUrl();
    return this.http.post(apiUrl, data);
  }

}
