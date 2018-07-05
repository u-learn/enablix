import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';

import { FreetextSearchService } from '../services/freetext-search.service';
import { AlertService } from '../core/alert/alert.service';
import { GlobalSearchControllerService } from '../core/search-bar/global-search-controller.service';

@Component({
  selector: 'ebx-freetext-search',
  templateUrl: './freetext-search.component.html',
  styleUrls: ['./freetext-search.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FreetextSearchComponent implements OnInit {

  searchResults: any;

  pageNum: number = 0;
  pageSize: number = 20;

  constructor(private route: ActivatedRoute, private textSearch: FreetextSearchService,
              private alert: AlertService, private globalSearchCtrl: GlobalSearchControllerService,
              private router: Router) { }

  ngOnInit() {


    this.route.params.subscribe(params => {
      
      let searchText = params['text'];
      
      if (searchText) {

        this.route.queryParams.subscribe(queryParams => {
          
          this.pageNum = queryParams['pageNum'] || 0;
          this.pageSize = queryParams['pageSize'] || 20;
          
          var searchInput = {
            request: {
              textQuery: searchText,
              pagination: {
                pageNum: this.pageNum,
                pageSize: this.pageSize
              }
            }
          };
          
          this.textSearch.searchBizContent(searchInput).subscribe(res => {
              this.searchResults = res;
            }, err => {
              this.alert.error("Unable to search. Please try later.", err.statusCode);
            });

        });

        this.globalSearchCtrl.setFreetextSearchBar(searchText);
      }
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
