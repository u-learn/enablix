import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { ActivityAuditService } from '../../core/activity-audit.service';
import { ApiUrlService } from '../../core/api-url.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';
import { TableColumn } from '../../core/model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { DataFilter, DataFiltersOptions, DataFiltersConfig, DataFilterValueTx, IdPropDataFilterValueTx } from '../../core/data-filters/data-filters.model';
import { OffsetDaysFilterValueTx } from '../model/report-config.model';
import { Utility } from '../../util/utility';
import { Constants } from '../../util/constants';

@Component({
  selector: 'ebx-activity-audit',
  templateUrl: './activity-audit.component.html',
  styleUrls: ['./activity-audit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ActivityAuditComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  dataFiltersConfig: DataFiltersConfig;
  lastUsedFilters: any;

  activityTypeNameMap: any = {};

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
    private alert: AlertService, private actvyAuditService: ActivityAuditService,
    private navService: NavigationService, private router: Router) { }

  ngOnInit() {
    
    let _this = this;

    var metricPeriods = [
        { label: 'Last 1 Day', id: 1 },
        { label: 'Last 3 Days', id: 3 },  
        { label: 'Last 7 Days', id: 7 },
        { label: 'Last 30 Days', id: 30 }
      ];

    let filters: DataFilter[] = [
      {
        id: "auditUser",
        type: "multi-select",
        name: "User",
        options: { },
        masterList: _this.getSystemUsers.bind(_this),
        defaultValue: function() { return null; },
        valueTx: IdPropDataFilterValueTx.theInstance
      },
      {
        id: "activityTypeIn",
        type: "multi-select",
        name: "Activity Type",
        options: { },
        masterList: _this.getActivityTypes.bind(_this),
        valueTx: ActivityTypeFilterValueTx.theInstance,
        defaultValue: function() { return null; }
      },
      {
        id: "auditEventOcc",
        type: "multi-select",
        options: {
          singleSelect: true
        },
        name: "Time",
        masterList: function() {
          return Observable.of(metricPeriods);
        },
        defaultValue: function() { return null; },
        valueTx: OffsetDaysFilterValueTx.theInstance
      }
    ];

    this.dataFiltersConfig = {
      filters: filters,
      options: {
        heading: "Filter",
        resetLabel: "Reset",
        searchLabel: "Go",
        prefValuesKey: null,
        searchOnLoad: true
      }
    }

    this.tableColumns = [
      {
        heading: "Details",
        key: "details"
      },
      {
        heading: "Activity Type",
        key: "activityType",
        sortProp: "activity.activityType"
      },
      {
        heading: "Date",
        key: "actvyDate",
        sortProp: "activityTime"
      },
      {
        heading: "User",
        key: "user",
        sortProp: "actor.name"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.DESC;
    this.pagination.sort.field = 'activityTime';
  }

  fetchFilteredData(filters: any) {
    this.pagination.pageNum = 0;
    this.fetchData(filters);
  }

  fetchData(filters: any) {
    
    this.lastUsedFilters = filters;
    Utility.removeNullProperties(filters.dataFilters);

    this.actvyAuditService.fetchActivities(filters.dataFilters, this.pagination).subscribe((data) => {
      this.dataPage = data;
    }, error => {
      this.alert.error("Error fetching activity data.", error.statusCode);
    });
  }

  getSystemUsers() : Observable<any> {
    
    let apiUrl = this.apiUrlService.getAllMembersUrl();
    
    return this.http.get(apiUrl).map((data: any) => {
      
      let userList = [];
      
      if (data) {
        data.forEach((user) => {
          userList.push({
            id: user.name,
            label: user.name
          });
        });
      }

      userList.push({
        label: "System",
        id: "System"
      });

      return userList;
    });
  }

  getActivityTypes() {
    return Observable.of(this.getActivityTypeList());
  }

  getActivityTypeList() {

    let actvyTypeList = [];
    this.activityTypeNameMap = this.actvyAuditService.getSearchActivityTypes();

    if (this.activityTypeNameMap) {
      
      let data = this.activityTypeNameMap;
      
      for (let key in data) {
        actvyTypeList.push({
          id: key,
          label: data[key]
        });
      }
    }

    Utility.sortArrayByLabel(actvyTypeList);
    ActivityTypeFilterValueTx.theInstance.actvyTypeList = actvyTypeList;

    return actvyTypeList;
  }

  getActivityDesc(record: any) : string {

    if (record.activity.category === "CONTENT"
          || record.activity.category === "NAVIGATION") {
      return record.activity.itemTitle;
      
    } else if (record.activity.category === "SEARCH") {
      
      if (record.activity.activityType === "SUGGESTED_SEARCH") {
      
        var suggType = record.activity.suggestionType;
        var retVal = "Accessed Via Search: ";
        
        if (suggType === "BizDimensionNode" || suggType === 'BizContentNode') {
          retVal += "All ";
        }
        
        return retVal + record.activity.searchTerm
      }
      return "Searched: " + record.activity.searchTerm;
      
    } else if (record.activity.activityType === "LOGIN") {
      return "User Logged In";
      
    } else if (record.activity.activityType === "LOGOUT") {
      return "User Logged Out";
    }
    
    return "";
  }

  getActivityTypeName(record: any) : string {
    if (record.activity.activityType === "SUGGESTED_SEARCH") {
      return "Search";
    }
    let name = this.activityTypeNameMap[record.activity.activityType];
    return name ? name : record.activity.activityType;
  }

  navToContent(record: any) {
    
    let containerQId = record.activity.containerQId;
    let contentIdentity = record.activity.itemIdentity;

    if (record.activity && record.activity.category === "CONTENT" && 
          !(record.activity.activityType === 'DOC_PREVIEW' 
            || record.activity.activityType === "DOC_DOWNLOAD" 
            || record.activity.activityType === 'DOC_UPLOAD')) {
      
      this.navService.goToRecordDetail(containerQId, contentIdentity, this.router.url);

    }

  }

}


class ActivityTypeFilterValueTx implements DataFilterValueTx {
  
  static theInstance: ActivityTypeFilterValueTx = new ActivityTypeFilterValueTx();

  actvyTypeList: any;

  transform(selectedValues: any) : any {
    
    let values = selectedValues;
    if (!values || values.length == 0) {
      values = this.actvyTypeList;
    }

    if (values && values.length > 0) {
    
      var returnVal = [];
      values.forEach((val) => {
        returnVal.push(val.id);
        if (val.id === "SEARCH_FREE_TEXT") {
          returnVal.push("SUGGESTED_SEARCH");
        }
      });

      return returnVal;
    }
    
    return null;
  }


}