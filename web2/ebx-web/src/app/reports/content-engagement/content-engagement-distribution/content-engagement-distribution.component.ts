import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';
import { NvD3Component } from 'ng2-nvd3';

declare let d3: any;

@Component({
  selector: 'ebx-content-engagement-distribution',
  templateUrl: './content-engagement-distribution.component.html',
  styleUrls: ['./content-engagement-distribution.component.scss', '../../../../../node_modules/nvd3/build/nv.d3.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContentEngagementDistributionComponent implements OnInit {

  chartOptions: any;

  metricData: any;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {

    this.metricData = this.data.metric;

    this.chartOptions = {
      chart: {
        type: 'pieChart',
        height: 500,
        x: function(d){return d.label;},
        y: function(d){return d.count;},
        showLabels: true,
        duration: 500,
        labelThreshold: 0.01,
        labelSunbeamLayout: true,
        legend: {
            margin: {
                top: 5,
                right: 35,
                bottom: 5,
                left: 0
            }
        }
      }
    };

  }

}
