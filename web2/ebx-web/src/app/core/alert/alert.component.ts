import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { AlertService } from './alert.service';
import { Alert, AlertType } from './alert.model';

@Component({
  selector: 'ebx-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AlertComponent implements OnInit {

  alerts: Alert[] = [];

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    
    this.alertService.getAlert().subscribe((alert: Alert) => {
    
      if (!alert) {
        // clear alerts when an empty alert is received
        this.alerts = [];
        return;
      }

      // add alert to array
      this.alerts.push(alert);
      
      // remove success alert after 5 sec
      if (alert.type == AlertType.SUCCESS) {
        setTimeout(() => {
          this.removeAlert(alert);
        }, 5000);
      }
    });
  }

  removeAlert(alert: Alert) {
    this.alerts = this.alerts.filter(x => x !== alert);
  }

  cssClass(alert: Alert) {
    
    if (!alert) {
        return;
    }

    // return css class based on alert type
    switch (alert.type) {
      case AlertType.SUCCESS:
        return 'alert alert-success';
      case AlertType.ERROR:
        return 'alert alert-danger';
      case AlertType.INFO:
        return 'alert alert-info';
      case AlertType.WARNING:
        return 'alert alert-warning';
    }
  }
}
