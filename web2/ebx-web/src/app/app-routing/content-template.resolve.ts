import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { ContentTemplate } from '../model/content-template.model';
import { ContentTemplateService } from '../core/content-template.service';
import { UserService } from '../core/auth/user.service';

@Injectable()
export class ContentTemplateResolve implements Resolve<ContentTemplate> {
  
  constructor(private contentTemplateService: ContentTemplateService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      ContentTemplate | Observable<ContentTemplate> | Promise<ContentTemplate> {

    return this.contentTemplateService.init();
  }

}