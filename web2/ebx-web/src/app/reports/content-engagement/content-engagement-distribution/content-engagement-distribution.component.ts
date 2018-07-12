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
        x: function(d) { return d.label + " (" + d.count + ")"; },
        y: function(d) { return d.count; },
        showLabels: true,
        duration: 500,
        labelThreshold: 0.01,
        labelSunbeamLayout: true,
        legend: {
        },
        tooltip: {
          contentGenerator: function(d) { 
            return '<table><tbody><tr><td class="legend-color-guide">' +
                    '<div style="background-color: ' + d.color + '"></div></td>' + 
                    '<td class="key">' + d.data.label + '</td><td class="value">' + d.data.count + '</td>' +
                    '</tr></tbody></table>';
          }
        }
      }
    };

  }

}
