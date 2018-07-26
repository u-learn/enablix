import { Component, OnInit, ViewEncapsulation, Inject, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormControl } from '@angular/forms';

import { SelectOption } from '../../../core/select/select.component';
import { SlackService } from '../../../services/slack.service';
import { AlertService } from '../../../core/alert/alert.service';

@Component({
  selector: 'ebx-slack-share-popup',
  templateUrl: './slack-share-popup.component.html',
  styleUrls: ['./slack-share-popup.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SlackSharePopupComponent implements OnInit {

  slackChannels: SelectOption[];
  channelsCtrl: FormControl;

  message: string;

  constructor(private dialogRef: MatDialogRef<SlackSharePopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private slack: SlackService,
              private alert: AlertService) { 
    this.channelsCtrl = new FormControl();
  }

  ngOnInit() {

    this.slack.getSlackChannels().subscribe(
      (res: any) => {
        this.slackChannels = [];
        if (res && res.channels) {
          res.channels.forEach(ch => {
            this.slackChannels.push({
              id: ch.id, 
              label: ch.name
            });  
          });
        }
      },
      (err) => {
        this.alert.error("Unable to get list of slack channels. Please try later.", err.statusCode);
      }
    );

  }

  share() {
    
    let channelIds = [];

    let channelOpts = this.channelsCtrl.value;

    if (channelOpts) {
      channelOpts.forEach(ch => {
        channelIds.push(ch.id);
      });
    }

    if (channelIds.length > 0) {
      this.slack.sendMessage(this.data.container.qualifiedId, 
            this.data.record.identity, channelIds, this.message).subscribe(
        res => {
          this.dialogRef.close(true);
          this.alert.success("Shared successfully on Slack.")
        }, 
        err => {
          this.alert.error("Error while sharing on Slack.", err.status);
        }
      );  
    }
    
  }

}
