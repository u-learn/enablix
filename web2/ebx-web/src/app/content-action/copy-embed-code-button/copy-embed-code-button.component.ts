import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { Utility } from '../../util/utility';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-copy-embed-code-button',
  templateUrl: './copy-embed-code-button.component.html',
  styleUrls: ['./copy-embed-code-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CopyEmbedCodeButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  buttonText = "Copy Embed Code";

  constructor(private route: ActivatedRoute, private apiUrlService: ApiUrlService) { }

  ngOnInit() {
  }

  copy() {

    if (this.record) {
    
      let navUrl = this.apiUrlService.getEmbedCodeUrl(this.container.qualifiedId, this.record.identity) + "?atChannel=CONTENTWIDGET&atContext=EMBEDCODE";
      let apiUrl = this.apiUrlService.postFetchContentWidgetUrl();

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
            this.buttonText = "Copy Embed Code";
          }, 4000);
      });

    }
  }

}
