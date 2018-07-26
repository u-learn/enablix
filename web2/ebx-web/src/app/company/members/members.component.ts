import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { MatDialog, MatDialogRef } from '@angular/material';

import { MembersService } from './members.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';
import { TableColumn, TableActionConfig, TableAction } from '../../core/model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { UserProfile } from '../../model/user.model';
import { Constants } from '../../util/constants';
import { MemberDetailComponent } from './member-detail/member-detail.component';

@Component({
  selector: 'ebx-members',
  templateUrl: './members.component.html',
  styleUrls: ['./members.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MembersComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  tableActions: MembersTableActions;

  constructor(
      private membersService: MembersService,
      private alert: AlertService,
      private dialog: MatDialog) { 
    this.tableActions = new MembersTableActions(this);
  }

  ngOnInit() {
    
    this.tableColumns = [
      {
        heading: "Name",
        key: "name",
        sortProp: "name"
      },
      {
        heading: "Email",
        key: "email",
        sortProp: "email"
      },
      {
        heading: "Roles",
        key: "roles"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.ASC;
    this.pagination.sort.field = 'name';

    this.filters = {};

    this.fetchData();
  }

  fetchData() {
    this.membersService.getMembers(this.filters, this.pagination).subscribe(res => {
      this.dataPage = res;
    }, error => {
      this.alert.error("Error fetching members list", error.status);
    });
  }

  getUserRoles(rec: any) : string {
    let roles = rec.systemProfile.roles;
                    
    let roleStr = "";
    for (let i = 0; i < roles.length; i++) {
      if (i > 0 && i == (roles.length - 1)) {
        roleStr += " and ";
      } else if (i != 0) {
        roleStr += ", ";
      }
      roleStr += roles[i].roleName;
    }
    
    return roleStr;
  }

  addMember(selectedRecords: UserProfile[]) : Observable<any> {
    
    let dialogRef = this.dialog.open(MemberDetailComponent, {
        width: '640px',
        maxHeight: '90vh',
        disableClose: true,
        data: { newRecord: true }
      });

    let sub = dialogRef.afterClosed();
    sub.subscribe(res => {
      if (res) {
        this.fetchData();
      }
    });

    return sub;
  }

  editMember(selectedMembers: UserProfile[]) : Observable<any> {
    
    if (selectedMembers && selectedMembers.length == 1) {
    
      let userProfile = selectedMembers[0];
      let dialogRef = this.dialog.open(MemberDetailComponent, {
          width: '640px',
          maxHeight: '90vh',
          disableClose: true,
          data: { newRecord: false, profileIdentity: userProfile.identity }
        });

      let sub = dialogRef.afterClosed().share();
      sub.subscribe(res => {
        if (res) {
          this.fetchData();
        }
      });  

      return sub;
    }

    return Observable.of(null);
  }

  deleteMembers(selectedMembers: UserProfile[]) : Observable<any> {
    
    let identities: string[] = selectedMembers.map(mem => mem.identity);
    
    let sub = this.membersService.deleteMembers(identities).share();
    
    sub.subscribe(res => {
      this.alert.success("User(s) deleted successfully.");
      this.fetchData();
    }, err => {
      this.alert.error("Unable to delete user(s). Please try later.", err.status);
    });
    
    return sub;
  }

  openMemberDetail(userProfile: UserProfile) {
    let dialogRef = this.dialog.open(MemberDetailComponent, {
        width: '640px',
        maxHeight: '90vh',
        disableClose: true,
        data: { newRecord: false, readOnly: true, profileIdentity: userProfile.identity }
      });

    let sub = dialogRef.afterClosed().share();
    sub.subscribe(res => {
      if (res) {
        this.fetchData();
      }
    }); 
  }

}

class MembersTableActions implements TableActionConfig<UserProfile> {

  component: MembersComponent;

  addAction: TableAction<UserProfile>;
  delAction: TableAction<UserProfile>;
  editAction: TableAction<UserProfile>;

  constructor(comp: MembersComponent) {
    
    this.component = comp;

    this.addAction = {
        label: "Add User",
        iconClass: "plus",
        successMessage: "Added",
        execute: this.component.addMember.bind(this.component)
      };

    this.delAction = {
        label: "Remove User",
        iconClass: "trash",
        successMessage: "Deleted",
        confirmConfig: {
          title: "Delete User(s)",
          confirmMsg: this.deleteConfirmMsg,
          okLabel: "Proceed",
          cancelLabel: "Cancel"
        },
        execute: this.component.deleteMembers.bind(this.component)
      };


    this.editAction = {
        label: "Edit User",
        iconClass: "edit",
        execute: this.component.editMember.bind(this.component)
      };

  }

  deleteConfirmMsg(selectedRecs: UserProfile[]) : string {
    
    if (selectedRecs.length == 1) {
    
      return "You are about to remove user " + selectedRecs[0].name 
          + ". You cannot undo this operation. Would you like to proceed?";

    } else {

      return "You are about to remove multiple users. "
          + "You cannot undo this operation. Would you like to proceed?";

    }

  }

  getAvailableActions(selectedRecords: any[]) : TableAction<any>[] {
    
    let actions = [this.addAction];

    if (selectedRecords) {
      if (selectedRecords.length === 1) {
        actions.push(this.editAction);
      }

      if (selectedRecords.length > 0) {
        actions.push(this.delAction);
      }
    }

    return actions;
  }
}
