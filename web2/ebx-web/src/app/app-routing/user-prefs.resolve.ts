import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { UserPreferenceService } from '../core/user-preference.service';

@Injectable()
export class UserPreferenceResolve implements Resolve<any> {
  
  constructor(private userPrefService: UserPreferenceService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      any | Observable<any> | Promise<any> {

    return this.userPrefService.init();
  }

}