import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'ebx-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SearchBarComponent implements OnInit {

  showOptions = false;

  constructor() { }

  ngOnInit() {
  }

  hideSearchBar() {
    this.showOptions = false;
  }

  showSearchBar() {
    this.showOptions = true;
  }

}
