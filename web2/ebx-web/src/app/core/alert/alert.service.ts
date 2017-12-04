import { Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import { Alert, AlertType } from './alert.model'; 

@Injectable()
export class AlertService {

  private subject = new Subject<Alert>();
  private keepAfterRouteChange = false;

  constructor(private router: Router) {
    // clear alert messages on route change unless 'keepAfterRouteChange' flag is true
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (this.keepAfterRouteChange) {
          // only keep for a single route change
          this.keepAfterRouteChange = false;
        } else {
          // clear alert messages
          this.clear();
        }
      }
    });
  }

  getAlert() : Observable<Alert> {
    return this.subject.asObservable();
  }

  success(message: string, keepAfterRouteChange = false) {
    this.alert(AlertType.SUCCESS, message, keepAfterRouteChange);
  }

  error(message: string, statusCode: number, keepAfterRouteChange = false) {
    if (statusCode != 401) {
      this.alert(AlertType.ERROR, message, keepAfterRouteChange);  
    }
    
  }

  info(message: string, keepAfterRouteChange = false) {
    this.alert(AlertType.INFO, message, keepAfterRouteChange);
  }

  warn(message: string, keepAfterRouteChange = false) {
    this.alert(AlertType.WARNING, message, keepAfterRouteChange);
  }

  alert(type: AlertType, message: string, keepAfterRouteChange = false) {
    this.keepAfterRouteChange = keepAfterRouteChange;
    this.subject.next(<Alert>{ type: type, message: message });
  }

  clear() {
    // clear alerts
    this.subject.next();
  }

}
