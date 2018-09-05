import { Component, OnInit, ViewEncapsulation, Inject, ViewChild, ElementRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { ContentBrowserSearchControllerService } from './content-browser-search-controller.service';

@Component({
  selector: 'ebx-content-browser',
  templateUrl: './content-browser.component.html',
  styleUrls: ['./content-browser.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentBrowserComponent implements OnInit {

  @ViewChild('infiniteScrollParentElem') public infiniteScrollParentElem: ElementRef;
  @ViewChild('infiniteScrollChildElem') public infiniteScrollChildElem: ElementRef;

  objectKeys = Object.keys;

  selectedItemGrps: any[];

  noData: boolean = false;

  groupByQId?: boolean = true;
  sortable?: boolean = false;

  constructor(
      public browserSearchCtrl: ContentBrowserSearchControllerService,
      private dialogRef: MatDialogRef<ContentBrowserComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    
    this.browserSearchCtrl.initBrowserSearchBar(this.data.scopeContainer);
    this.selectedItemGrps = [];

    this.groupByQId = this.data.groupByQId;
    this.sortable = this.data.sortable;

    if (this.data.selectedItems) {
      this.data.selectedItems.forEach(item => {
        this.addItemToGrp(item);
      });
    }

    console.log(this.selectedItemGrps);
    this.browserSearchCtrl.onBrowserDataUpdate.subscribe(this.onBrowserDataUpdate.bind(this));

  }

  private addItemToGrp(item: any) {
    
    var grpEntry = null;
    var groupingId = this.getGroupId(item);
    var groupLabel = this.getGroupLabel(item);

    for(var i = 0; i < this.selectedItemGrps.length; i++) {
      var grp = this.selectedItemGrps[i];
      if (grp.qId == groupingId) {
        grpEntry = grp;
        break;
      }
    }

    if (!grpEntry) {
      grpEntry = {
        qId: groupingId,
        label: groupLabel,
        items: []
      }
      this.selectedItemGrps.push(grpEntry);
    }

    grpEntry.items.push(item);
  }

  getGroupId(item: any) {
    return this.groupByQId ? item.qualifiedId : "~all~";
  }

  getGroupLabel(item: any) {
    return this.groupByQId ? item.containerLabel : null;
  }

  done() {
    var result = [];
    this.selectedItemGrps.forEach((grp) => {
      grp.items.forEach((item) => result.push(item));
    });
    this.dialogRef.close(result);
  }

  deleteItemIfFound(item: any) {
    
    var deleted = false;

    var itemLoc = this.findItem(item);

    if (itemLoc && itemLoc.index >= 0) {
      if (itemLoc.list) {
        itemLoc.list.splice(itemLoc.index, 1);
        deleted = true;
      }
    }

    return deleted;
  }

  findItem(item: any) {
    
    var itemLocation = null;
    var itemGrpId = this.getGroupId(item);

    for(var i = 0; i < this.selectedItemGrps.length; i++) {
    
      var grp = this.selectedItemGrps[i];
    
      if (grp.qId == itemGrpId) {
    
        for (var k = 0; k < grp.items.length; k++) {
    
          var grpItem = grp.items[k];
          
          if (grpItem.identity === item.identity) {
            
            itemLocation = {
              list: grp.items,
              index: k
            };

            break;
          }
        }
        break;
      }
    }

    return itemLocation;
  }


  toggleSelect(rec: any) {
    var item = this.convertRecordToItem(rec);
    if (!this.deleteItemIfFound(item)) {
      this.addItemToGrp(item);
    }
  }

  convertRecordToItem(rec: any) {
    return {
      identity: rec.record.identity,
      label: rec.record.__title,
      qualifiedId: rec.containerQId,
      containerLabel: rec.record.__container
    }
  }

  removeItem(item: any) {
    this.deleteItemIfFound(item);
  }

  isSelected(rec: any) {
    var item = this.convertRecordToItem(rec);
    if (this.findItem(item)) return true;
    return false;
  }

  getTotalSelectedItemCount() {
    var cnt = 0;
    this.selectedItemGrps.forEach((grp) => {
      cnt += grp.items.length;
    });
    return cnt;
  }

  onScroll() {
    console.log('scrolled!!');
    this.browserSearchCtrl.checkAndFetchData(true);
  }

  onBrowserDataUpdate() {
    
    this.noData = !this.browserSearchCtrl.browserData 
      || this.browserSearchCtrl.browserData.length == 0;

    if (this.browserSearchCtrl.hasNextPage) {
      this.callbackUntilScrollable();
    }
  }

  private callbackUntilScrollable() {
    window.setTimeout(() => {
      if (this.infiniteScrollParentElem.nativeElement.clientHeight > this.infiniteScrollChildElem.nativeElement.scrollHeight) {
          this.onScroll();
      }
    }, 500);  // If you use loading-placeholder.
  }

}
