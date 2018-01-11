import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { UserService } from '../core/auth/user.service';
import { AuthService } from '../core/auth/auth.service';

@Injectable()
export class AuthGuard implements CanActivate {
  
  constructor(private router: Router, private user: UserService, private authService: AuthService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

  	if (this.user.isUserLoggedIn()) {
  		return true;
  	}

    if (this.initFromLocalstorage()) {
      
      return true;

    } else {

      return this.authService.loginUser(null, null, null).map(res => {
        if (this.initFromLocalstorage()) {
          return true;
        }
        this.router.navigate(['login'], { queryParams: {returnUrl: state.url} });
        return false;
      });
    }
    
  }

  private initFromLocalstorage() : boolean {
    
    let userJSON = localStorage.getItem('currentUser');
    
    if (userJSON != null) {
      let currentUser = JSON.parse(userJSON);
      this.authService.initUserService(currentUser);
      return true;
    }

    return false;
  }
}
