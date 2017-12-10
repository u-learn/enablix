import { Injectable, Output, EventEmitter, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Response, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { BootController } from '../../../boot-control';
import { ApiUrlService } from '../api-url.service';
import { UserService } from './user.service';

@Injectable()
export class AuthService {

  @Output() loginIn: EventEmitter<any> = new EventEmitter();

  constructor(private http: HttpClient,
    private ngZone: NgZone, 
    private router: Router, 
    private apiUrlService: ApiUrlService, 
    private userService: UserService) { }

  loginUser(username: string, password: string, rememberMe: boolean) {
    
    this.loginIn.emit(null);

  	return this.http.get(this.apiUrlService.getUserUrl(), {headers: this.loginRequestHeaders(username, password, rememberMe)})
                		.map((res: any) => {
                			let user = res;
                      if (user && user.name) {
                          // store user details in local storage to keep user logged in between page refreshes
                          localStorage.setItem('currentUser', JSON.stringify(user));
                        	this.initUserService(user);
                      }
                      return user;
                		});
  }

  initUserService(user: any) {
    this.userService.setUserLoggedIn(user);
    this.loginIn.emit(user.name);
  }

  private loginRequestHeaders(username: string, password: string, rememberMe: boolean): HttpHeaders {
  	let headers = new HttpHeaders();
  	headers = headers.append("Authorization", "Basic " + btoa(username + ":" + password));
    if (rememberMe) {
      headers = headers.append("remember-me", "true");
    }
  	return headers;
  }


  clearLoginInfo() {
    this.userService.logoutUser();
    localStorage.removeItem('currentUser');
    this.loginIn.emit(null);
  }

  logoutUser() {
  	
  	// backend logout logic
  	// HACK: to clear basic auth associated with browser window, 
	  // update it with a bad credentials
	  // http://stackoverflow.com/questions/233507/how-to-log-out-user-from-web-site-using-basic-authentication
  	this.http.get(this.apiUrlService.getLogoutUrl(), 
          {headers: this.loginRequestHeaders('~~baduser~~', '~~', false)})
      .subscribe(
        resp => {
          console.log("Logout success...");
          
          this.clearLoginInfo();
          this.redirectToLogin();
          return resp;
        }, 
        err => {
          console.log("Logout error ...");
          this.loginIn.emit(null);
          this.clearLoginInfo();
          this.redirectToLogin();
        });

  }

  redirectToLogin() {
    
    console.log("Rebooting...");

    // Triggers the reboot in main.ts        
    this.ngZone.runOutsideAngular(() => BootController.getbootControl().restart());

    this.router.navigate(['login']);
  }

  isAuthenticated() {
  	return this.userService.isUserLoggedIn();
  }

}
