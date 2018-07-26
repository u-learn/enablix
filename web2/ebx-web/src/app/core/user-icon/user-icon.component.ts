import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from '../auth/user.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'ebx-user-icon',
  templateUrl: './user-icon.component.html',
  styleUrls: ['./user-icon.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UserIconComponent implements OnInit {

  initial: string;
  showOpts: boolean = false;

  constructor(private userService: UserService, private authService: AuthService) { }

  ngOnInit() {
    this.initial = this.userService.getUserDisplayName().charAt(0).toUpperCase();
  }

  logout() {
    this.authService.logoutUser();
  }

  navToSettings() {
    
  }

  showOptions() {
    this.showOpts = true;
  }

  hideOptions() {
    this.showOpts = false;
  }

}
