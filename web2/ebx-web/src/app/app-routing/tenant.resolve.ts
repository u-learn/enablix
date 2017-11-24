import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Tenant } from '../model/tenant.model';
import { TenantService } from '../services/tenant.service';

@Injectable()
export class TenantResolve implements Resolve<Tenant> {
  
  constructor(private tenantService: TenantService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      Tenant | Observable<Tenant> | Promise<Tenant> {

    return this.tenantService.loadTenant();
  }

}