import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';

import { ApiUrlService } from '../core/api-url.service';
import { AlertService } from './alert/alert.service';

@Injectable()
export class UserPreferenceService {

  userPrefs: any = null;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private alert: AlertService) { }

  
  init() : Observable<any> {
    
    if (this.userPrefs) {
      return Observable.of(this.userPrefs);
    } 

    return this.loadApplicablePrefs();  
  }

  loadApplicablePrefs() {
    return this.http.get<any>(this.apiUrlService.getApplicableUserPrefsUrl())
           .map((prefs: any) => { 
                this.userPrefs = {};
                 if (prefs) {
                   prefs.forEach((pref) => {
                     this.userPrefs[pref.key] = pref;
                   });
                 }
                 return this.userPrefs;
               },
               error => {
                 this.alert.error("Error loading user preferences", error.status);
               }
             );
  }
    
  getPrefByKey(prefKey: string) {
    return this.userPrefs ? this.userPrefs[prefKey] : null;
  }
  
  saveOrUpdatePref(url: string, prefKey: string, prefValue: any) : Observable<any> {
      
    var userPref = {
         key: prefKey,
         config: prefValue
    }
      
    return this.http.post(url, userPref).map((data: any) => {
         this.userPrefs[prefKey] = userPref;
         return data;
       });
       
  }
    
  saveAsUserPref(prefKey: string, prefValue: any) : Observable<any> {
    let apiUrl = this.apiUrlService.postSaveAsUserPrefUrl();
    return this.saveOrUpdatePref(apiUrl, prefKey, prefValue);
  }
    
  saveAsSystemPref(prefKey: string, prefValue: any) : Observable<any> {
    let apiUrl = this.apiUrlService.postSaveAsSystemPrefUrl();
    return this.saveOrUpdatePref(apiUrl, prefKey, prefValue);
  }
}
