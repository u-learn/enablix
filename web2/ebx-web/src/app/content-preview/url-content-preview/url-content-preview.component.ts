import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { EmbedInfoService } from '../../upload/embed-info.service';
import { EmbedInfo } from '../../model/embed-info.model';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-url-content-preview',
  templateUrl: './url-content-preview.component.html',
  styleUrls: ['./url-content-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UrlContentPreviewComponent implements OnInit {

  @Input() record: any;

  url: string;
  embedInfo: EmbedInfo;

  type: string;
  embedHtml: string;
  thumbnailUrl: string;
  navUrl: string;

  constructor(private embedInfoService: EmbedInfoService,
              private apiUrlService: ApiUrlService) { }

  ngOnInit() {

    if (this.record && this.record.__urls && this.record.__urls[0]) {
    
      this.url = this.record.__urls[0].url;
    
      if (this.url) {
    
        this.navUrl = this.apiUrlService.getExtLinkUrl(this.url);

        this.embedInfoService.getEmbedInfo(this.url)
            .subscribe(
                result => {
                  
                  this.embedInfo = result;
                  this.type = this.embedInfoService.getEmbedInfoType(this.embedInfo);

                  if (this.type != 'rich' && this.embedInfo.oembed && this.embedInfo.oembed) {
                    this.embedHtml = this.embedInfo.oembed.html;

                  } else {
                    this.thumbnailUrl = this.embedInfoService.getThumbnailUrl(this.embedInfo);
                  }
                },
                error => {
                  console.log(error);
                  // TODO: handle error
                }
              );
      }
    }
  }

}
