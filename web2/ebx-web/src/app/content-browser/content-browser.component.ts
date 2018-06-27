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

  selectedItems: any;

  constructor(
      public browserSearchCtrl: ContentBrowserSearchControllerService,
      private dialogRef: MatDialogRef<ContentBrowserComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    
    this.browserSearchCtrl.initBrowserSearchBar();
    this.selectedItems = {};

    if (this.data.selectedItems) {
      this.data.selectedItems.forEach(item => {
        this.selectedItems[item.identity] = item;
      });
    }

    this.browserSearchCtrl.onBrowserDataUpdate.subscribe(this.onBrowserDataUpdate.bind(this));

  }

  done() {
    
    var result = [];
    for (let key in this.selectedItems) {
      result.push(this.selectedItems[key]);
    }

    this.dialogRef.close(result);
  }

  toggleSelect(rec: any) {
    
    var recIdentity = rec.record.identity;
    
    if (this.selectedItems[recIdentity]) {
      
      delete this.selectedItems[recIdentity];

    } else {
    
      this.selectedItems[recIdentity] = {
        identity: recIdentity,
        label: rec.record.__title,
        qualifiedId: rec.containerQId,
        containerLabel: rec.record.__container
      }
    }
  }

  removeItem(itemIdentity: string) {
    if (this.selectedItems[itemIdentity]) {
      delete this.selectedItems[itemIdentity];
    }
  }

  isSelected(rec: any) {
    return this.selectedItems[rec.record.identity];
  }

  onScroll() {
    console.log('scrolled!!');
    this.browserSearchCtrl.checkAndFetchData(true);
  }

  onBrowserDataUpdate() {
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
