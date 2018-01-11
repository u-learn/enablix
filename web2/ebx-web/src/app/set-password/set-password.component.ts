import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AuthService } from '../core/auth/auth.service';
import { AlertService } from '../core/alert/alert.service';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-set-password',
  templateUrl: './set-password.component.html',
  styleUrls: ['./set-password.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SetPasswordComponent implements OnInit {

  @ViewChild('setPwdForm') form;

  setPwd: any;
  password: string;
  confirmPassword: string;

  returnUrl: string;
  passwordUpdated = false;

  constructor(private authService: AuthService, private alert: AlertService,
      private navService: NavigationService, private router: Router, 
      private route: ActivatedRoute,) { }

  ngOnInit() {
    
    this.setPwd = {
      password: null,
      confirmPassword: null
    };

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  setPassword() {
    
    if (this.setPwd.password !== this.setPwd.confirmPassword) {
      
      this.form.form.controls.confirmPassword.setErrors({mismatch: true});

    } else {

      this.authService.setPassword(this.setPwd.password).subscribe(res => {
        this.alert.success("Password updated successfully", true);
        this.router.navigateByUrl(this.returnUrl);
      }, err => {
        this.alert.error("Error updating password. Please try again later.", err.status);
      });

    }
  }

}
