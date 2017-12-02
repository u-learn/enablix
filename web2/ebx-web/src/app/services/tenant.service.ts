import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../core/api-url.service';
import { Tenant } from '../model/tenant.model';

@Injectable()
export class TenantService {

  tenant: Tenant;

  constructor(private http:HttpClient, private urlService:ApiUrlService) { 
  }

  loadTenant() : Observable<Tenant> {
    let apiUrl = this.urlService.getTenantUrl();
    return this.http.get<Tenant>(apiUrl).map(res => {
      this.tenant = res;
      return this.tenant
    });
  }

}
