import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { GlobalSearchControllerService } from '../core/search-bar/global-search-controller.service';
import { Utility } from '../util/utility';
import { SalesforceCtx } from './salesforce-context';
import { ContentService } from '../core/content/content.service';
import { AlertService } from '../core/alert/alert.service';

@Component({
  selector: 'ebx-salesforce-home',
  templateUrl: './salesforce-home.component.html',
  styleUrls: ['./salesforce-home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SalesforceHomeComponent implements OnInit {

  linkedContent: any;

  constructor(private globalSearchCtrl: GlobalSearchControllerService,
    private route: ActivatedRoute, private contentService: ContentService,
    private alert: AlertService) { }

  ngOnInit() {
    
    this.globalSearchCtrl.setDashboardSearchBar();
    
    let queryParams = this.route.snapshot.queryParams;
    
    if (queryParams["ctx_reset"]) {
      SalesforceCtx.reset();
    }

    if (!SalesforceCtx.isInitialized()) {
      let sfCtx: any = Utility.readAppCtxInQueryParams(this.route.snapshot.queryParams);
      SalesforceCtx.init(sfCtx);
    }

    let contentRequest = SalesforceCtx.getLinkedMappedContentRequest();
    if (contentRequest) {
      this.contentService.getLinkdedAndMappedContent(contentRequest).subscribe(
          result => {
            console.log(result);
            this.linkedContent = result;
          },
          err => {
            this.alert.error("Error fetching record related content", err.statusCode);
          }
        );
    }

  }

}
