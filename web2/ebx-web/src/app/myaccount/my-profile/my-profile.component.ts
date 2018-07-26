import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { UserService } from '../../core/auth/user.service';
import { AlertService } from '../../core/alert/alert.service';
import { MembersService } from '../../company/members/members.service';

@Component({
  selector: 'ebx-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MyProfileComponent implements OnInit {

  user: any;
  editing: boolean;

  constructor(private userService: UserService,
      private alert: AlertService, private membersService: MembersService) { }

  ngOnInit() {
    this.user = this.userService.getUserProfile();
  }

  initEdit() {
    this.editing = true;
  }

  cancelEdit() {
    this.editing = false;
  }

  updateUserProfile() {
    this.membersService.updateMember(this.user).subscribe(res => {
      this.user = res;
      this.alert.success("User Profile updated successfully.");
      this.userService.updateUserProfile(this.user);
      this.editing = false;
    }, err => {
      this.alert.error("Error updating User Profile. Please try later.", err.status);
    });
  }  


}
