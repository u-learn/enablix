import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { AuthService } from '../core/auth/auth.service';
import { AlertService } from '../core/alert/alert.service';

@Component({
  selector: 'ebx-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ForgotPasswordComponent implements OnInit {

  username: string;

  invalidUsername: boolean = false;
  passwordUpdated: boolean = false;

  constructor(private authService: AuthService, private alert: AlertService) { }

  ngOnInit() {
  }

  resetPassword(input: any) {

    if (this.username && this.username.trim().length > 0) {
      
      this.authService.resetPassword(this.username).subscribe(res => {

        if (!res) {
          this.invalidUsername = true;
        } else {
          this.passwordUpdated = true;
        }
      }, err => {
        this.alert.error("Unable to reset password. Please try again later.", err.status);
      });

    }

  }

}
