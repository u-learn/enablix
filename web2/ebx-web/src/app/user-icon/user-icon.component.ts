import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from '../core/auth/user.service';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'ebx-user-icon',
  templateUrl: './user-icon.component.html',
  styleUrls: ['./user-icon.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UserIconComponent implements OnInit {

  initial: string;
  constructor(private router: Router, private userService: UserService, private authService: AuthService) { }

  ngOnInit() {
    this.initial = this.userService.getUserDisplayName().charAt(0).toUpperCase();
  }

  logout() {
    this.authService.logoutUser()
      .subscribe(
        resp => {
          this.router.navigate(['login']);
      },
      error => {
        this.router.navigate(['login']);      
      });
  }

}
