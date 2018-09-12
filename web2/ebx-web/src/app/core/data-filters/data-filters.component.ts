import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { cloneDeep } from 'lodash';

import { DataFiltersConfig, DataFilter, DataFiltersOptions } from './data-filters.model';
import { UserPreferenceService } from '../user-preference.service';
import { AlertService } from '../alert/alert.service';

@Component({
  selector: 'ebx-data-filters',
  templateUrl: './data-filters.component.html',
  styleUrls: ['./data-filters.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DataFiltersComponent implements OnInit {

  options: DataFiltersOptions;
  filters: DataFilter[];

  _config: DataFiltersConfig;

  @Input() layout?: string = 'top';
  @Output() onSearch = new EventEmitter<any>();
  @Output() onReset = new EventEmitter<void>(); 

  filterOptions: any = {};
  filterValues: any = {};

  filterErrMessages: any = {};
  filterValuesBackup: any = {};

  selected = ['data-filters'];

  constructor(private userPrefService: UserPreferenceService,
    private alert: AlertService) { }

  @Input()
  set config(cfg: DataFiltersConfig) {
    
    var defaultOptions: DataFiltersOptions = {
      heading: "Filters",
      resetLabel: "Reset",
      searchLabel: "Search",
      hideFiltersWithNoOptions: false,
      persistSelection: false,
      showSaveAsDefault: false,
      prefValuesKey: null,
      searchOnLoad: false
    };

    this.options = Object.assign(defaultOptions, cfg.options);
    if (this.options.prefValuesKey) {
      this.options.showSaveAsDefault = true;
    }

    this.filters = cfg.filters;
    this.filterOptions = {}
    
    if (this.filters) {
    
      this.filters.forEach((filter) => {
        if (filter.masterList) {
          filter.masterList().subscribe((data: any) => {
            this.filterOptions[filter.id] = data;
          });
        }
      });

      this.initSearchFilters();

      this.filterValuesBackup = cloneDeep(this.filterValues);

      if (this.options.searchOnLoad) {
        this.searchAction();
      }
    }
  }

  get config() {
    return this._config;
  }

  ngOnInit() {
  }

  getSearchFilterValues() {
        
    var searchValues = {};
    
    var filterValuesValid = true;
    this.filterErrMessages = {};

    this.filters.forEach((filter) => {
      
      let errors = null;

      if (filter.validator) {
        errors = filter.validator.validate(this.filterValues[filter.id]);
      }
      
      if (errors && errors.length > 0) {
      
        filterValuesValid = false;

        let errMsg = "";
        errors.forEach((err) => {
          errMsg += err.message + "\n "
        });

        this.filterErrMessages[filter.id] = errMsg;

      } else {
        
        let filterVal = this.filterValues[filter.id];
        searchValues[filter.id] = filter.valueTx ? 
            filter.valueTx.transform(filterVal) : filterVal;
      }
      
    });
    
    return filterValuesValid ? searchValues : null;
  }

  initSearchFilters() {
        
    var defaultPrefFVs = this.options.prefValuesKey ? 
        this.userPrefService.getPrefByKey(this.options.prefValuesKey) : null;
    
    var searchFilters = {};
    this.filters.forEach((filter) => {
      
      if (this.filterValues) {
        
        // check user preferences first
        var defaultFV = defaultPrefFVs ? defaultPrefFVs.config[filter.id] : null;
        
        if (!defaultFV && filter.defaultValue) {
          // check report definition if not present in user pref
          defaultFV = filter.defaultValue();
        }

        if (defaultFV) {
          
          this.filterValues[filter.id] = defaultFV;
          
          searchFilters[filter.id] = filter.valueTx ? 
              filter.valueTx.transform(defaultFV) : defaultFV;
        }
      }
    });
  }

  searchAction() {
    var searchValues = this.getSearchFilterValues();
    if (searchValues) {
      this.onSearch.emit({
        dataFilters: searchValues,
        filterSelection: this.filterValues
      });
    }
  }

  resetAction() {
    if (this.filterValuesBackup) {
      this.filterValues = cloneDeep(this.filterValuesBackup);
    }
    this.onReset.emit();
  }

  getCurrentFilterValues() {
        
    var filterVals = {};
    
    this.filters.forEach((filter) => {
      filterVals[filter.id] = this.filterValues[filter.id];
    });
    
    return filterVals;
  }

  saveAsSystemDefault() {
        
    if (this.options.prefValuesKey) {
      
      var filterVals = this.getCurrentFilterValues();
      
      this.userPrefService.saveAsSystemPref(this.options.prefValuesKey, filterVals).subscribe(
          (data) => {
            this.alert.success("Default values updated successfully.");
            this.filterValuesBackup = cloneDeep(filterVals);
          }, error => {
            this.alert.error("Unable to update default values. Please try later.", error.statusCode);
          }
        );
    }
  }

}
