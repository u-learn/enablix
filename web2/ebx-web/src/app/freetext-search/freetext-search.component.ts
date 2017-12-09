import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { FreetextSearchService } from '../services/freetext-search.service';
import { AlertService } from '../core/alert/alert.service';
import { SearchBarService } from '../search-bar/search-bar.service';

@Component({
  selector: 'ebx-freetext-search',
  templateUrl: './freetext-search.component.html',
  styleUrls: ['./freetext-search.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FreetextSearchComponent implements OnInit {

  searchResults: any;

  constructor(private route: ActivatedRoute, private textSearch: FreetextSearchService,
              private alert: AlertService, private sbService: SearchBarService) { }

  ngOnInit() {


    this.route.params.subscribe(params => {
      
      let searchText = params['text'];
      
      if (searchText) {

        this.route.queryParams.subscribe(queryParams => {
          
          let pageNum = queryParams['pageNum'] || 0;
          let pageSize = queryParams['pageSize'] || 20;
          
          this.textSearch.searchBizContent(searchText, pageNum, pageSize).subscribe(res => {
              this.searchResults = res;
            }, err => {
              this.alert.error("Unable to search. Please try later.", err.statusCode);
            });

        });

        this.sbService.setFreetextSearchBar(searchText);
      }
    });
  }

}
