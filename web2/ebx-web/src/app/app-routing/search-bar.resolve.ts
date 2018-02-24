import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { SearchBarData } from '../model/search-bar-data.model';
import { SearchBarService } from '../core/search-bar/search-bar.service';

@Injectable()
export class SearchBarResolve implements Resolve<SearchBarData> {
  
  constructor(private sbService: SearchBarService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): 
      SearchBarData | Observable<SearchBarData> | Promise<SearchBarData> {

    return this.sbService.init();
  }

}