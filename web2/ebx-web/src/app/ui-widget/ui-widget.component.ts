import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { UiWidgetService } from '../services/ui-widget.service';

@Component({
  selector: 'ebx-ui-widget',
  templateUrl: './ui-widget.component.html',
  styleUrls: ['./ui-widget.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UiWidgetComponent implements OnInit {

  @Input() widgetId: string;

  widgetDetails: any;

  constructor(private uiWdgtService: UiWidgetService) { }

  ngOnInit() {
    this.uiWdgtService.getWidgetData(this.widgetId).subscribe(
      res => {
        this.widgetDetails = res;
      },
      err => {
        console.log("Error fetching UI widgets");
      }
    );
  }

}
