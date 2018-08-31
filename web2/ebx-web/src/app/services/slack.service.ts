import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';
import { AlertService } from '../core/alert/alert.service';

@Injectable()
export class SlackService {

  static SLACK_AUTH_URL = "https://slack.com/oauth/authorize?scope=chat:write:user,channels:read&client_id=";

  constructor(private http: HttpClient, 
    private apiUrlService: ApiUrlService,
    private alert: AlertService) { }

  getSlackStoredToken() {
    let url = this.apiUrlService.getSlackStoredToken();
    return this.http.get(url);
  }

  getSlackAppDetails() {
    let url = this.apiUrlService.getSlackAppDetails();
    return this.http.get(url); 
  }

  saveSlackAuthCode(authCode: string, redirectURI: string) {  
    let url = this.apiUrlService.postSaveSlackAuthCode();
    return this.http.post(url, {}, { params: { code: authCode, redirect_uri: redirectURI } });
  }

  removeSlackAuthCode() {
    let url = this.apiUrlService.postDeleteSlackAuthCode();
    return this.http.post(url, {});
  }

  getSlackChannels() {
    let url = this.apiUrlService.getSlackChannels();
    return this.http.get(url);
  }

  sendMessage(containerQId: string, contentIdentity: string, slackChannelIds: any, message: string) {
    var shareSlackData = {
      "containerQId" : containerQId,
      "contentIdentity": contentIdentity,
      "channelsSelectd": slackChannelIds,
      "slackCustomContent" : message ? message : ""
    };

    let url = this.apiUrlService.postSlackMessage();
    return this.http.post(url, {}, { params: shareSlackData });
  }

}
