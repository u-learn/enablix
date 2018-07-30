import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

import { TableColumn } from '../../../core/model/table.model';
import { DataType } from '../../../core/data-search/filter-metadata.model';

@Component({
  selector: 'ebx-opp-attribution',
  templateUrl: './opp-attribution.component.html',
  styleUrls: ['./opp-attribution.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class OppAttributionComponent implements OnInit {

  metricData: any;
  tableColumns: TableColumn[];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    if (this.data.metric) {
      this.metricData = { content: this.data.metric.attributionItems };  
    }
    

    this.tableColumns = [
      {
        heading: "Opportunity",
        key: "oppName",
        headerCssClass: "small-font content-title-col"
      },
      {
        heading: "Dollar Value",
        key: "dollarValue",
        dataType: DataType.NUMBER,
        headerCssClass: "small-font oa-dollar-val"
      },
      {
        heading: "Status",
        key: "oppStatus",
        headerCssClass: "small-font"
      },
      {
        heading: "Downloads",
        key: "downloads",
        dataType: DataType.NUMBER,
        headerCssClass: "small-font oa-download"
      },
      {
        heading: "Shares",
        key: "shares",
        dataType: DataType.NUMBER,
        headerCssClass: "small-font oa-share"
      }
    ];
  }

  getStatusName(statusCode: string) {
    for (var i = 0; i < this.data.statusTx.length; i++) {
      var status = this.data.statusTx[i];
      if (status.id == statusCode) {
        return status.label;
      }
    }
    return statusCode;
  }

}
