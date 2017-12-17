import { Component, OnInit, ViewEncapsulation, Inject, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { AlertService } from '../../../core/alert/alert.service';
import { MembersService } from '../members.service';
import { UserProfile, UserBusinessProfile, UserSystemProfile } from '../../../model/user.model';
import { Container } from '../../../model/container.model';
import { ContentTemplateService } from '../../../core/content-template.service';

@Component({
  selector: 'ebx-member-detail',
  templateUrl: './member-detail.component.html',
  styleUrls: ['./member-detail.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MemberDetailComponent implements OnInit {

  @ViewChild('memberForm') form;

  newUserOper: boolean = false;
  userProfile: UserProfile;
  allRoles: any[];

  roleOptions: CheckboxOption[];
  userContainer: Container;

  constructor(
    private membersService: MembersService,
    private alert: AlertService,
    private ctService: ContentTemplateService,
    private dialogRef: MatDialogRef<MemberDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    
    this.newUserOper = this.data.newRecord;
    this.userContainer = this.ctService.getContainerByQId('user');

    if (this.newUserOper) {
      this.userProfile = new UserProfile();
      this.userProfile.businessProfile = new UserBusinessProfile();
      this.userProfile.businessProfile.attributes = {};
      this.userProfile.systemProfile = new UserSystemProfile();

    } else {
      
      this.membersService.getMemberProfile(this.data.profileIdentity).subscribe(res => {
      
        this.userProfile = res;
      
        if (!this.userProfile.systemProfile) {
          this.userProfile.systemProfile = new UserSystemProfile();
        }

        if (!this.userProfile.businessProfile) {
          this.userProfile.businessProfile = new UserBusinessProfile();
          this.userProfile.businessProfile.attributes = {};
        }

        this.updateRoleOptions();

      }, err => {
        this.alert.error("Error getting member details. Please try later.", err.status);
        this.dialogRef.close();
      });
    }

    this.membersService.getAllSystemRoles().subscribe((res: any) => {
      
      this.allRoles = res;
      this.roleOptions = [];
      
      this.allRoles.forEach(role => {
        this.roleOptions.push({
          value: role.identity,
          label: role.roleName,
          checked: false
        });
      });

      this.updateRoleOptions();

    }, err => {
      this.alert.error("Error fetching system roles.", err.status);
    });
  }

  updateRoleOptions() {
    
    if (this.allRoles && this.userProfile && this.userProfile.systemProfile 
          && this.userProfile.systemProfile.roles) {
      
      this.userProfile.systemProfile.roles.forEach(role => {
        
        for (let i = 0; i < this.roleOptions.length; i++) {
          if (this.roleOptions[i].value === role.identity) {
            this.roleOptions[i].checked = true;
            break;
          }
        }

      });

    }
  }

  save() {

    if (!this.userProfile.systemProfile) {
      this.userProfile.systemProfile = new UserSystemProfile();
    }

    let roles: any[] = [];
    if (this.roleOptions) {
      for (let i = 0; i < this.roleOptions.length; i++) {
        if (this.roleOptions[i].checked) {
          roles.push({
            identity: this.allRoles[i].identity
          });
        }
      }
    }

    if (!this.userProfile.systemProfile) {
      this.userProfile.systemProfile = new UserSystemProfile();
    }

    this.userProfile.systemProfile.roles = roles;
    
    console.log(this.userProfile);

    if (this.newUserOper) {
      
      this.membersService.addMember(this.userProfile).subscribe(res => {
        this.alert.success("Member added successfully.");
        this.dialogRef.close(true);
      }, err => {
        this.alert.error("Error adding member. Please try later.", err.status);
      });  

    } else {
      
      this.membersService.updateMember(this.userProfile).subscribe(res => {
        this.alert.success("Member updated successfully.");
        this.dialogRef.close(true);
      }, err => {
        this.alert.error("Error updating member. Please try later.", err.status);
      });
    }
    
  }

  checkUserExist() {
    this.membersService.checkUserExist(this.userProfile.email).subscribe((res: any) => {
      if (res.exist) {
        this.form.form.controls.email.setErrors({userexist: true});
      }
    });
  }

}

class CheckboxOption {
  value: string;
  label: string;
  checked: boolean;
}
