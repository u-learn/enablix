import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { SearchBarService } from '../core/search-bar/search-bar.service';

@Component({
  selector: 'ebx-consolidate-content',
  templateUrl: './consolidate-content.component.html',
  styleUrls: ['./consolidate-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ConsolidateContentComponent implements OnInit {

  constructor(private sbService: SearchBarService) { }

  ngOnInit() {
    this.sbService.setDashboardSearchBar();
  }

}
