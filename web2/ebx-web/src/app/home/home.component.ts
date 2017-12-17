import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { AlertService } from '../core/alert/alert.service';
import { SearchBarService } from '../search-bar/search-bar.service';

@Component({
  selector: 'ebx-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  constructor(private alertService: AlertService, 
    private searchBarService: SearchBarService,
    private http: HttpClient) { }

  ngOnInit() {
    this.searchBarService.setDashboardSearchBar();
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
