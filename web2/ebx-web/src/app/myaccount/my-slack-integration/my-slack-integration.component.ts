import { Component, OnInit, OnDestroy, ViewEncapsulation, Renderer2 } from '@angular/core';

import { SlackService } from '../../services/slack.service';
import { AlertService } from '../../core/alert/alert.service';
import { environment } from '../../../environments/environment';
import { Utility } from '../../util/utility';

@Component({
  selector: 'ebx-my-slack-integration',
  templateUrl: './my-slack-integration.component.html',
  styleUrls: ['./my-slack-integration.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MySlackIntegrationComponent implements OnInit, OnDestroy {

  slackToken: any;
  slackTokenLoaded: boolean;

  stopListening: Function;

  defaultDomain: string = environment.domainUrl;
  callingDomain: string;
  redirectURI: string = environment.domainUrl + "/slackauth.html";
  popupWindow: any;

  constructor(private slack: SlackService, private alert: AlertService,
      private renderer: Renderer2) { }

  ngOnInit() {

    this.callingDomain = window.location.protocol + "//" + window.location.host;
    this.stopListening = this.renderer.listen('window', 'message', this.handleMessage.bind(this));

    this.slack.getSlackStoredToken().subscribe(
      (res: any) => {
        console.log(res);
        this.slackToken = res;
        this.slackTokenLoaded = true;
      },
      (err: any) => {
        console.log(err);
      }
    );
  }

  authorizeSlack() {
    
    if (this.popupWindow && !this.popupWindow.closed) {

      this.popupWindow.focus();

    } else {

      this.slack.getSlackAppDetails().subscribe(
        (res: any) => {
          if (res) {
           
            let slackUrl = SlackService.SLACK_AUTH_URL + res.clientID 
                + "&redirect_uri=" + this.redirectURI + "&state=" + this.callingDomain;
            this.popupWindow = Utility.openPopupInCenter(slackUrl, "Slack Authorization", '600', '600');
          }
        },
        (err: any) => {
          this.alert.error("Unable to initiate Slack Authorization. Please try later.", err.statusCode);
        }
      );

    }
  }

  unauthorizeSlack() {

    this.slack.removeSlackAuthCode().subscribe(
      (res: any) => {
        this.slackToken = null;
        this.alert.success("Removed Slack authorization.");
      },
      (err: any) => {
        this.alert.error("Error removing Slack authorization. Please try later.", err.statusCode);
      }
    );

  }

  ngOnDestroy() {
    if (this.stopListening) {
      this.stopListening();
    }
  }

  handleMessage(msg) {
    
    console.log("Window message...");
    console.log(msg);

    if (msg.origin == this.defaultDomain) {
      
      if (msg.data.error) {
        
        this.alert.error("Unable to obtain authorization from Slack", 400);

      } else if (msg.data.code) {
        
        this.slack.saveSlackAuthCode(msg.data.code, this.redirectURI).subscribe(
          (res: any) => {
            this.slackToken = res;
            this.alert.success("Slack authorization successful.");
          },
          (err: any) => {
            this.alert.error("Unable to save Slack authorization. Please try again.", err.statusCode);
          }
        );
      }
    }
  }

}
