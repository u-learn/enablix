import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { DataPage } from '../model/data-page.model';
import { RecentContentService } from './recent-content.service';
import { AlertService } from '../core/alert/alert.service';
import { NavigationService } from '../app-routing/navigation.service';
import { ContentTemplateService } from '../core/content-template.service';

@Component({
  selector: 'ebx-recent-content',
  templateUrl: './recent-content.component.html',
  styleUrls: ['./recent-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecentContentComponent implements OnInit {

  data: DataPage;

  @Input() layout?: string = "widget";
  @Input() pagesize?: number = 5;
  @Input() bizContentOnly: boolean = true;

  constructor(private recentContentService: RecentContentService, 
    private alert: AlertService, private navService: NavigationService,
    private ctService: ContentTemplateService,
    private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.recentContentService.fetchRecentContent(this.bizContentOnly, this.pagesize)
        .subscribe(
            result => {
              this.data = result;
            },
            error => {
              this.alert.error("Error fetching recent content.", error.status);
            }
          );
  }

  getTextDescription(act: any) : string {
    return act.createdByName + (act.updateType == 'NEW' ? ' added ' : ' updated ') +
            act.data.title;
  }

  getIconUrl(act: any) : string {
    return this.recentContentService.getIconUrl(act);
  }

  getContentColor(act: any) : string {
    return this.recentContentService.getContentColor(act);
  }

  navToItemDetail(act: any) {    
    this.navService.goToRecordDetail(act.data.contentQId, act.data.recordIdentity, this.router.url);
  }

}
