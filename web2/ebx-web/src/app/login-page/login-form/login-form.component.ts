import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AuthService } from '../../core/auth/auth.service';
import { UserService } from '../../core/auth/user.service';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginFormComponent implements OnInit {

  username: string;
  password: string;
  rememberMe: boolean;

  returnUrl: string;
  invalidCredentials: boolean
  loginSuccess: boolean = false;

  constructor(private authService: AuthService, private router: Router, 
    private route: ActivatedRoute, private alert: AlertService, private user: UserService) {
  	this.username = "";
  	this.password = "";
    this.invalidCredentials = false;
  }

  ngOnInit() {
  	// reset login status
    this.authService.clearLoginInfo();
 
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  loginUser(ev) {
  	
  	ev.preventDefault();
  	
    this.authService.loginUser(this.username, this.password, this.rememberMe)
  	  .subscribe(
  			data => {
          
          this.loginSuccess = true;
          
          if (!this.user.isPasswordSet()) {
            this.router.navigate(['setpassword'], { queryParams: {returnUrl: this.returnUrl} });
          } else {
  				  this.router.navigateByUrl(this.returnUrl);
          }
  			},
  			error => {
  				if (error.status == 401) {
            this.invalidCredentials = true;
          } else {
            this.alert.error("Error in login. Please try again later.", error.status);
          }
  			}
  		);
  	
  }

}
