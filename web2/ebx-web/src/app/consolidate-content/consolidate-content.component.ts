import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { GlobalSearchControllerService } from '../core/search-bar/global-search-controller.service';

@Component({
  selector: 'ebx-consolidate-content',
  templateUrl: './consolidate-content.component.html',
  styleUrls: ['./consolidate-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ConsolidateContentComponent implements OnInit {

  constructor(private globalSearchCtrl: GlobalSearchControllerService) { }

  ngOnInit() {
    this.globalSearchCtrl.setDashboardSearchBar();
  }

}
