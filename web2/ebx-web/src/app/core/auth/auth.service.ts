import { Injectable, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Response, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { ApiUrlService } from '../api-url.service';
import { UserService } from './user.service';

@Injectable()
export class AuthService {

  @Output() loginIn: EventEmitter<any> = new EventEmitter();

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService, private userService: UserService) { }

  loginUser(username: string, password: string, rememberMe: boolean) {

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
    this.userService.setUserLoggedIn();
    this.userService.setUsername(user.email);
    this.userService.setUserDisplayName(user.name);
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

  logoutUser() {
  	
  	this.userService.logoutUser();
  	localStorage.removeItem('currentUser');
    localStorage.removeItem('resource-versions');

  	this.loginIn.emit(null);

  	// backend logout logic
  	// HACK: to clear basic auth associated with browser window, 
	  // update it with a bad credentials
	  // http://stackoverflow.com/questions/233507/how-to-log-out-user-from-web-site-using-basic-authentication
  	return this.http.get(this.apiUrlService.getUserUrl(), 
                         {headers: this.loginRequestHeaders('~~baduser~~', '~~', false)})
  					.map(res => { 
  						/* map causing the request to execute and logout user */
  						return res;
  					});
  }

  isAuthenticated() {
  	return this.userService.isUserLoggedIn();
  }

}
