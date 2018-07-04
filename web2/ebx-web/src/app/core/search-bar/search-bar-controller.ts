import { EventEmitter } from '@angular/core';
import { SearchBarData, SearchBarItem, SearchDataset } from '../../model/search-bar-data.model';

export interface SearchBarController {
  
  onSearchBarDataUpdate() : EventEmitter<void>;

  searchBarData(): SearchBarData;

  addSearchBarItem(sbItem: SearchBarItem);

  removeSearchBarItem(sbItem: SearchBarItem);

  searchInputId() : string;

  typeaheadItemSelected(bizItem: any);

  doFreetextSearch(text: string);

  clearFreetext();

}