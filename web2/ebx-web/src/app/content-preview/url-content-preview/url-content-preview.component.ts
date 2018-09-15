import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { EmbedInfoService } from '../../core/upload/embed-info.service';
import { EmbedInfo } from '../../model/embed-info.model';
import { Container } from '../../model/container.model';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-url-content-preview',
  templateUrl: './url-content-preview.component.html',
  styleUrls: ['./url-content-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UrlContentPreviewComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  url: string;
  embedInfo: EmbedInfo;

  type: string;
  embedHtml: string;
  thumbnailUrl: string;
  videoUrl: string;
  navUrl: string;
  iframeNotSupported: boolean;

  iframeLoadDone: boolean;

  constructor(private embedInfoService: EmbedInfoService,
              private apiUrlService: ApiUrlService) { }

  ngOnInit() {

    if (this.record && this.record.__urls && this.record.__urls[0]) {
    
      this.url = this.record.__urls[0].url;
    
      if (this.url) {
    
        this.navUrl = this.apiUrlService.getExtLinkUrl(this.url, this.record.identity, this.container.qualifiedId);
        var urlProtocolBlocked = this.urlProtocolNotSupported(this.url);

        this.embedInfoService.getEmbedInfo(this.url)
            .subscribe(
                result => {
                  
                  if (result) {
                    
                    this.embedInfo = result;
                    this.type = this.embedInfoService.getEmbedInfoType(this.embedInfo);
                    this.iframeNotSupported = !this.embedInfo.iframeEmbeddable
                          || urlProtocolBlocked;

                    if (this.type != 'rich' && this.type != 'link' 
                        && this.embedInfo.oembed && this.embedInfo.oembed.html) {
                      this.embedHtml = this.embedInfo.oembed.html;
                      this.type = 'embed-html';
                    } else if (this.type == 'video' && this.embedInfo.videos && this.embedInfo.videos[0]) {
                      this.videoUrl = this.embedInfo.videos[0].url;
                      if (this.videoUrl.indexOf("vimeo.com") > 0) {
                        this.embedHtml = "<iframe src=\"" + this.videoUrl + "\" width=\"640\" height=\"360\" frameborder=\"0\" title=\"GovCon Suite User Training - B&amp;P Budgeting Report 2 of 2\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>";
                        this.type = "embed-html";
                      }
                    } else if (this.embedInfo.html) {
                      this.embedHtml = this.embedInfo.html;

                    } else {
                      this.thumbnailUrl = this.embedInfoService.getThumbnailUrl(this.embedInfo);
                    }

                  } else {

                    if (this.url && !urlProtocolBlocked && this.url.toLowerCase().endsWith(".pdf")) {
                      this.type = "pdf";
                    } else {
                      this.type = "unknown";
                    }
                  }

                },
                error => {
                  this.type = "unknown";
                }
              );
      }
    }
  }

  urlProtocolNotSupported(url: string) {
    var windowProtocol = window.location.protocol;
    if (windowProtocol == 'https:' && url.startsWith('http://')) {
      return true;
    }
    return false;
  }

}
