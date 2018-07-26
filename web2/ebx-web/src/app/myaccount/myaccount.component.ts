import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { SlackService } from '../services/slack.service';
import { AlertService } from '../core/alert/alert.service';
import { environment } from '../../environments/environment';
import { UserService } from '../core/auth/user.service';

@Component({
  selector: 'ebx-myaccount',
  templateUrl: './myaccount.component.html',
  styleUrls: ['./myaccount.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MyaccountComponent implements OnInit {

  constructor() { 
  }

  ngOnInit() {
  }

}
