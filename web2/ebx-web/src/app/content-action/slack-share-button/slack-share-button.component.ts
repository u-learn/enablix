import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { NavigationService } from '../../app-routing/navigation.service';
import { AlertService } from '../../core/alert/alert.service';
import { SlackSharePopupComponent } from './slack-share-popup/slack-share-popup.component';
import { SlackService } from '../../services/slack.service';
import { MessageDialogComponent } from '../../core/message-dialog/message-dialog.component';

@Component({
  selector: 'ebx-slack-share-button',
  templateUrl: './slack-share-button.component.html',
  styleUrls: ['./slack-share-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SlackShareButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  constructor(private alert: AlertService, private dialog: MatDialog,
      private slack: SlackService, private navService: NavigationService) { }

  ngOnInit() {
  }

  openSharePopup() {
    this.slack.getSlackStoredToken().subscribe(
      (res: any) => {
        
        if (res && res.accessToken && res.teamName) {
          
          let dialogRef = this.dialog.open(SlackSharePopupComponent, {
            width: '624px',
            disableClose: true,
            autoFocus: false,
            data: { record: this.record, container: this.container }
          });

        } else {
          
          var dialogRef = this.dialog.open(MessageDialogComponent, {
            width: '624px',
            disableClose: true,
            data: { 
              title: "Slack Integration",
              text: "Please complete Slack authorization to Share on Slack.",
              okLabel: "OK",
              type: "INFO"
            }
          });

          dialogRef.afterClosed().subscribe(res => {
            this.navService.goToMyAccount();
          });
          
        }
      },
      (err) => {
        this.alert.error("Unable to share on Slack. Please try later.", err.statusCode);
      }
    );

    
  }

}
