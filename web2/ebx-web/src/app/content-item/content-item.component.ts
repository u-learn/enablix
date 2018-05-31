import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { DatePipe } from '@angular/common';

import { ContentItem } from '../model/content-item.model';
import { ContentTemplateService } from '../core/content-template.service';

@Component({
  selector: 'ebx-content-item',
  templateUrl: './content-item.component.html',
  styleUrls: ['./content-item.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentItemComponent implements OnInit {

  @Input() contentItem: ContentItem;
  @Input() record: any;
  @Input() editable?: boolean = false; 

  dateFormat: string = "MMM d, y";

  constructor(public ctService: ContentTemplateService,
    public datePipe: DatePipe) { }

  ngOnInit() {
  }

  getValue() : any {
    
    switch(this.contentItem.type) {

      case 'RICH_TEXT': 
        let pt = this.record[this.contentItem.id];
        let rt = this.record[this.contentItem.id + '_rt'];
        return rt ? rt : pt;

      case 'BOUNDED':
        
        let returnVal = null;

        let itemVal = this.record[this.contentItem.id];        
        if (itemVal) {
        
          returnVal = "";
        
          for (var i = 0; i < itemVal.length; i++) {
            if (i > 0) {
              returnVal += ", "
            }
            returnVal += itemVal[i].label
          }
        }

        return returnVal;

      case 'DATE_TIME':
        let dateVal = this.record[this.contentItem.id];
        if (dateVal) {
          return this.datePipe.transform(dateVal, this.dateFormat);
        }
        return null;
    
      default:
        return this.record[this.contentItem.id];
    }
    
  }

}
