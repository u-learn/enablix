import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { ContentTemplateService } from '../core/content-template.service';
import { ResourceVersionService } from '../core/versioning/resource-version.service';
import { ResourceVersion } from '../core/versioning/resource-version.model';


@Injectable()
export class ResourceVersionResolve implements Resolve<ResourceVersion[]> {
  
  constructor(private resourceVersionService: ResourceVersionService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      ResourceVersion[] | Observable<ResourceVersion[]> | Promise<ResourceVersion[]> {

    return this.resourceVersionService.loadResourceVersions();
  }

}