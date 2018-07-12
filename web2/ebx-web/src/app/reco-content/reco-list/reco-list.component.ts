import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';

import { AlertService } from '../../core/alert/alert.service';
import { RecoContentService } from '../reco-content.service';

@Component({
  selector: 'ebx-reco-list',
  templateUrl: './reco-list.component.html',
  styleUrls: ['./reco-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecoListComponent implements OnInit {

  recoList: any;

  pageNum: number = 0;
  pageSize: number = 20;

  constructor(private route: ActivatedRoute, private recoContentService: RecoContentService,
              private alert: AlertService, private router: Router) { }

  ngOnInit() {

    this.route.queryParams.subscribe(queryParams => {
      
      this.pageNum = queryParams['pageNum'] || 0;
      this.pageSize = queryParams['pageSize'] || 20;

      this.recoContentService.fetchRecoContent(this.pageSize, this.pageNum).subscribe(res => {
          this.recoList = res;
        }, err => {
          this.alert.error("Unable to fetch recommendations. Please try later.", err.statusCode);
        });

    });
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
