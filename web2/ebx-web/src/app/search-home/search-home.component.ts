import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { GlobalSearchControllerService } from '../core/search-bar/global-search-controller.service';

@Component({
  selector: 'ebx-search-home',
  templateUrl: './search-home.component.html',
  styleUrls: ['./search-home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SearchHomeComponent implements OnInit {

  constructor(private globalSearchCtrl: GlobalSearchControllerService) { }

  ngOnInit() {
    this.globalSearchCtrl.setDashboardSearchBar();
  }

}
