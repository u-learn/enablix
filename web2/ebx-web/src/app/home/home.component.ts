import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { AlertService } from '../core/alert/alert.service';

@Component({
  selector: 'ebx-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  constructor(private alertService: AlertService) { }

  ngOnInit() {
  }

  success(message: string) { 
    this.alertService.success(message);
  }

  error(message: string) {
    this.alertService.error(message);
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

}
