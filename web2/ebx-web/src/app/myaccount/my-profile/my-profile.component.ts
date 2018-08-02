import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';

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

  @ViewChild('myProfileForm') form;

  user: any;
  userBackup: any;
  editing: boolean;

  constructor(private userService: UserService,
      private alert: AlertService, private membersService: MembersService) { }

  ngOnInit() {
    let userString = JSON.stringify(this.userService.getUserProfile());
    this.user = JSON.parse(userString);
    this.userBackup = JSON.parse(userString);
  }

  initEdit() {
    this.editing = true;
  }

  cancelEdit() {
    this.editing = false;
    this.user = JSON.parse(JSON.stringify(this.userBackup));
  }

  updateUserProfile() {

    if (!this.form.valid) {
      return;
    }

    this.membersService.updateMember(this.user).subscribe(res => {
      this.alert.success("User Profile updated successfully.");
      this.userService.updateUserProfile(this.user);
      this.ngOnInit();
      this.editing = false;
    }, err => {
      this.alert.error("Error updating User Profile. Please try later.", err.status);
    });
  }  


}
