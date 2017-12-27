import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms';
import "rxjs/add/operator/share";

import { SearchBarService } from './search-bar.service';
import { SearchBarData, SearchBarItem, SearchDataset } from '../model/search-bar-data.model';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SearchBarComponent implements OnInit {

  showOptions = false;
  doNotHideOnClick = false;
  defaultDSItemLimit = 4;
  dsItemLimit: { [key : string] : number} = {};

  textCtrl: FormControl;
  freeText: string;

  searchBarData: SearchBarData;

  filteredDSData: { [key: string] : Observable<SearchBarItem[]>} = {};

  constructor(private sbService: SearchBarService, private navService: NavigationService) { 
    this.textCtrl = new FormControl();
  }

  ngOnInit() {
    
    this.updateSearchBarData();
    
    this.sbService.onSearchBarDataUpdate.subscribe(data => {
      this.updateSearchBarData();
    });

  }

  private updateSearchBarData() {
    this.searchBarData = this.sbService.searchBarData;

    if (this.searchBarData && this.searchBarData.datasets) {

      this.searchBarData.datasets.forEach(ds => {
        if (!this.dsItemLimit[ds.getDatasetLabel()]) {
          this.dsItemLimit[ds.getDatasetLabel()] = this.defaultDSItemLimit;
        }
      })
    
      this.textCtrl.valueChanges.startWith(null).subscribe(
        text => {
          this.freeText = text;
          this.searchBarData.datasets.forEach(ds => {
            let itemObs = ds.getDataItems(text);
            this.filteredDSData[ds.getDatasetLabel()] = itemObs ? itemObs.share() : itemObs;
          });
        });
    }
  }

  expandDatasetItems(ds: SearchDataset) {
    this.dsItemLimit[ds.getDatasetLabel()] = 100;
    this.doNotHideOnClick = true;
  }

  collapseDatasetItems(ds: SearchDataset) {
    this.dsItemLimit[ds.getDatasetLabel()] = this.defaultDSItemLimit;
    this.doNotHideOnClick = true; 
  }

  toggleSearchBar() {
    this.showOptions = !this.showOptions;
  }

  hideSearchBar() {
    if (!this.doNotHideOnClick) {
      this.showOptions = false;
    } else {
      this.doNotHideOnClick = false;
    }
  }

  showSearchBar() {
    this.showOptions = true;
  }

  addSearchBarItem(sbItem: SearchBarItem) {
    this.sbService.addSearchBarItem(sbItem);
  }

  removeSearchBarItem(sbItem: SearchBarItem) {
    this.sbService.removeSearchBarItem(sbItem);
    this.doNotHideOnClick = true;
  }

  searchBizContent() {
    if (this.freeText) {
      this.navService.goToFreetextSearch(this.freeText);  
      this.textCtrl.setValue(null);
    }
  }

  clearFreetext() {
    this.searchBarData.freetext = null;
  }

}