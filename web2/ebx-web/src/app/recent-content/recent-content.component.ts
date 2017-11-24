import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { DataPage } from '../model/data-page.model';
import { RecentContentService } from './recent-content.service';
import { AlertService } from '../core/alert/alert.service';

@Component({
  selector: 'ebx-recent-content',
  templateUrl: './recent-content.component.html',
  styleUrls: ['./recent-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecentContentComponent implements OnInit {

  data: DataPage;

  constructor(private recentContentService: RecentContentService, private alert: AlertService) { }

  ngOnInit() {
    this.recentContentService.fetchRecentContent()
        .subscribe(
            result => {
              this.data = result;
              console.log(result);
            },
            error => {
              this.alert.error("Error fetching recent content.");
            }
          );
  }

}
