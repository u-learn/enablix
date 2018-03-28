import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { ActivityAuditService } from '../core/activity-audit.service';

@Injectable()
export class AuditActivityResolve implements Resolve<any> {
  
  constructor(private actvyAuditService: ActivityAuditService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      any | Observable<any> | Promise<any> {

    return this.actvyAuditService.init();
  }

}