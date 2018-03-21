import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { Utility } from '../../util/utility';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-copy-download-url-button',
  templateUrl: './copy-download-url-button.component.html',
  styleUrls: ['./copy-download-url-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CopyDownloadUrlButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  buttonText = "Copy File URL";

  constructor(private route: ActivatedRoute, private apiUrlService: ApiUrlService) { }

  ngOnInit() {
  }

  copy() {

    if (this.record.__decoration && this.record.__decoration.__docMetadata) {
    
      let docIdentity = this.record.__decoration.__docMetadata.identity;
      let navUrl = this.apiUrlService.getShareDocDownloadUrl(docIdentity) + "?atChannel=WEB&atContext=COPYNSHARE";
      let apiUrl = this.apiUrlService.postFetchShareDocUrl();

      var auditRequest = {
        "containerQId" : this.container.qualifiedId,
        "instanceIdentity" : this.record.identity,
        "itemTitle" : this.record._title
      };
      
      var request = {
        "url" : navUrl,
        "auditRequest" : auditRequest
      };

      Utility.syncPostAjaxCall(apiUrl, request, (res: any) => {
          
          Utility.copyToClipboard(res.shareableUrl);
      
          this.buttonText = "Copied to Clipboard";
          
          setTimeout(() => {
            this.buttonText = "Copy File URL";
          }, 4000);
      });

    }
  }

}
