import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AlertService } from '../../../core/alert/alert.service';
import { UiWidgetService } from '../../../services/ui-widget.service';

@Component({
  selector: 'ebx-cp-widget-detail',
  templateUrl: './cp-widget-detail.component.html',
  styleUrls: ['./cp-widget-detail.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CpWidgetDetailComponent implements OnInit {

  details: any;

  pageNum: number;
  pageSize: number;

  widgetId: string;

  constructor(private router: Router,
    private route: ActivatedRoute,
    private uiWdgtService: UiWidgetService,
    private alert: AlertService) { }

  ngOnInit() {
    
    this.widgetId = this.route.snapshot.params['widgetId'];

    this.route.queryParams.subscribe(queryParams => {
      
      this.pageNum = queryParams['pageNum'] || 0;
      this.pageSize = queryParams['pageSize'] || 20;

      this.fetchData();

    });
  }

  fetchData() {

    if (this.widgetId) {
      
      this.uiWdgtService.getWidgetData(this.widgetId, this.pageNum, this.pageSize).subscribe(
        res => {
          this.details = res;
        },
        err => {
          this.alert.error("Error fetching data. Please try again later.", err.statusCode);
        }
      );
    }
  }

  setPage(pageNum: number) {
    // Object.assign is used as apparently 
    // you cannot add properties to snapshot query params
    const queryParams: Params = Object.assign({}, this.route.snapshot.queryParams);

    // Do sth about the params
    queryParams['pageNum'] = pageNum;

    this.router.navigate([], { queryParams: queryParams });
  }

}
