import { Component, OnInit, ViewEncapsulation, Input, ViewChild } from '@angular/core';
import { environment } from '../../../environments/environment';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-doc-download-button',
  templateUrl: './doc-download-button.component.html',
  styleUrls: ['./doc-download-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DocDownloadButtonComponent implements OnInit {

  @ViewChild("downloadLink") downloadLink; 

  _record: any;
  @Input() container: Container;

  downloadUrl: string;

  constructor(private alert: AlertService, private apiUrlService: ApiUrlService) { }

  @Input()
  set record(rec: any) {
    this._record = rec;
    if (rec) {
      this.downloadUrl = this.apiUrlService.getDocDownloadUrl(this._record.__decoration.__docMetadata.identity) + "?atChannel=WEB";
    }
  }

  get record() {
    return this._record;
  }

  ngOnInit() {
  }

  docDownload() {
    if (this.downloadLink) {
      this.downloadLink.nativeElement.click();
    }
  }
}
