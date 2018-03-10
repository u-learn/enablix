import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { Utility } from '../../util/utility';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-copy-link-url-button',
  templateUrl: './copy-link-url-button.component.html',
  styleUrls: ['./copy-link-url-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CopyLinkUrlButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  buttonText = "Copy Url";

  constructor(private route: ActivatedRoute, private apiUrlService: ApiUrlService) { }

  ngOnInit() {
  }

  copy() {

    if (this.record.__urls && this.record.__urls.length > 0) {
    
      let url = this.record.__urls[0].url;
      let navUrl = this.apiUrlService.getShareExtLinkUrl(url, this.record.identity, this.container.qualifiedId);
      let apiUrl = this.apiUrlService.postFetchShareLinkUrl();

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
            this.buttonText = "Copy Url";
          }, 4000);
      });

    }
  }

}
