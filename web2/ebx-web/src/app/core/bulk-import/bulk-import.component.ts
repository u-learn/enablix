import { Component, OnInit, ViewEncapsulation, ChangeDetectorRef } from '@angular/core';

import { ImportRecord, ImportRequest } from './bulk-import.model';

@Component({
  selector: 'ebx-bulk-import',
  templateUrl: './bulk-import.component.html',
  styleUrls: ['./bulk-import.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BulkImportComponent implements OnInit {

  /**
    Steps:
    1. Select source
    2. Select files to import
    3. Edit selected import records
   **/

  step: number = 1;
  importRequest: ImportRequest;

  constructor(private changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
    
  }

  filesSelected() {
    this.step = 2;
    this.changeDetector.detectChanges();
  }

  importRequestReady(request: ImportRequest) {
    console.log("Files import request: ");
    console.log(request);
    this.step = 3;
    this.importRequest = request;
    this.changeDetector.detectChanges();
  }

  importRequestSubmitted(data?: any) {
    this.step = 4;
    this.changeDetector.detectChanges();
  }

  addMoreFiles() {
    this.step = 1;
    this.changeDetector.detectChanges();
  }

}
