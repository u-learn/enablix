import { Component, OnInit, ViewEncapsulation, Inject, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormControl } from '@angular/forms';

import { MembersService } from '../../../company/members/members.service';
import { SelectOption } from '../../../core/select/select.component';
import { ContentShareService } from '../../../services/content-share.service';
import { AlertService } from '../../../core/alert/alert.service';

@Component({
  selector: 'ebx-email-share-popup',
  templateUrl: './email-share-popup.component.html',
  styleUrls: ['./email-share-popup.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EmailSharePopupComponent implements OnInit {

  members: SelectOption[];
  membersCtrl: FormControl;

  message: string;

  constructor(private dialogRef: MatDialogRef<EmailSharePopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private membersService: MembersService,
              private shareService: ContentShareService,
              private alert: AlertService) { 
    this.membersCtrl = new FormControl();
  }

  ngOnInit() {

    this.members = [];

    this.membersService.getAllMembers().subscribe((res: any[]) => {
      res.forEach(member => {
        this.members.push({
          id: member.email, 
          label: member.name + " <" + member.email + ">"
        });  
      });
    });

  }

  send() {
    
    let emailIds = [];

    let emailOpts = this.membersCtrl.value;

    if (emailOpts) {
      emailOpts.forEach(email => {
        emailIds.push(email.id);
      });
    }

    if (emailIds.length > 0) {
      this.shareService.shareByEmail(this.data.container.qualifiedId, 
      this.data.record.identity, emailIds, this.message).subscribe(res => {
          this.dialogRef.close(true);
          this.alert.success("Mail sent successfully.")
        }, err => {
          this.alert.error("Error sending email", err.status);
        });  
    }
    
  }

}
