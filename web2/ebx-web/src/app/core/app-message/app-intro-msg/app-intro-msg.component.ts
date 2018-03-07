import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { UserService } from '../../auth/user.service';

@Component({
  selector: 'ebx-app-intro-msg',
  templateUrl: './app-intro-msg.component.html',
  styleUrls: ['./app-intro-msg.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppIntroMsgComponent implements OnInit {

  username: string;

  constructor(private user: UserService) { }

  ngOnInit() {
    this.username = this.user.getUserDisplayName();
  }

}
