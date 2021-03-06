import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

import { AlertService } from '../core/alert/alert.service';
import { GlobalSearchControllerService } from '../core/search-bar/global-search-controller.service';
import { AppMessageService } from '../core/app-message/app-message.service';
import { AppIntroMsgComponent } from '../core/app-message/app-intro-msg/app-intro-msg.component';
import { UserPreferenceService } from '../core/user-preference.service';


@Component({
  selector: 'ebx-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  widgetIds: string[];
  hideRecommendations = false;

  constructor(private alertService: AlertService, 
    private globalSearchCtrl: GlobalSearchControllerService,
    private http: HttpClient, private route: ActivatedRoute,
    private appMsgService: AppMessageService,
    private userPrefs: UserPreferenceService) { }

  ngOnInit() {
    this.globalSearchCtrl.setDashboardSearchBar();
    var newSignup = this.route.snapshot.queryParams["ns"];
    if (newSignup === "1") {
      setTimeout(() => {
        this.appMsgService.show(AppIntroMsgComponent);
      });
    }

    var widgets = this.userPrefs.getPrefByKey('dashboard.widgets');
    if (widgets && widgets.config) {

      if (widgets.config.widgetIds) {
        this.widgetIds = widgets.config.widgetIds;
      }

      if (widgets.config.hideRecommendations) {
        this.hideRecommendations = widgets.config.hideRecommendations;
      }
    }

  }

  success(message: string) { 
    this.alertService.success(message);
  }

  error(message: string) {
    this.alertService.error(message, 0);
  }

  info(message: string) {
    this.alertService.info(message);
  }

  warn(message: string) {
    this.alertService.warn(message);
  }

  clear() {
    this.alertService.clear();
  }

  corsTest() {
    this.http.post("http://13.126.45.107/api/v1/user/create", {
      emailId: "cors1@gmail.com",
      password: "password",
      name: "Cors Issue"
    }).subscribe(res => {
         console.log(res); 
      }, err => {
        console.log(err);
      })
  }
}
