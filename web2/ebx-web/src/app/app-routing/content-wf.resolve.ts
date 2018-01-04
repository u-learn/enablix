import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Tenant } from '../model/tenant.model';
import { ContentWorkflowService } from '../services/content-workflow.service';

@Injectable()
export class ContentWFResolve implements Resolve<any> {
  
  constructor(private cwService: ContentWorkflowService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      any | Observable<any> | Promise<any> {

    return this.cwService.init();
  }

}