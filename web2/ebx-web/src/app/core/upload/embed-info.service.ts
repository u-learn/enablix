import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../api-url.service';
import { EmbedInfo } from '../../model/embed-info.model';
import { EmbeddedUrl } from '../../model/embedded-url.model';
import { UrlCaptureInfo } from '../../model/upload-info.model';

@Injectable()
export class EmbedInfoService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  getEmbedInfo(url: string) : Observable<EmbedInfo> {
    let apiUrl = this.apiUrlService.getEmbedInfoUrl();
    return this.http.get<EmbedInfo>(apiUrl, { params: { url: url } });
  }

  getThumbnailUrl(embedInfo: EmbedInfo) : string {
    
    if (embedInfo.images && embedInfo.images[0]) {
      return embedInfo.images[0].url;
    } else if (embedInfo.oembed) {
      return embedInfo.oembed.thumbnailUrl;
    }

    return null;
  }

  toEmbeddedUrl(embedInfo: EmbedInfo) : EmbeddedUrl {
    
    let embeddedUrl = null;
    
    if (embedInfo) {
      embeddedUrl = new EmbeddedUrl();
      embeddedUrl.previewImageUrl = this.getThumbnailUrl(embedInfo);
      embeddedUrl.title = embedInfo.title;
      embeddedUrl.url = embedInfo.url;
      embeddedUrl.type = this.getEmbedInfoType(embedInfo);
    }

    return embeddedUrl;
  }

  defaultEmbeddedUrl(urlInfo: UrlCaptureInfo) {

    let embeddedUrl = null;
    
    if (urlInfo) {
      embeddedUrl = new EmbeddedUrl();
      embeddedUrl.previewImageUrl = "/assets/images/icons/url-icon.svg"
      embeddedUrl.url = urlInfo.url;
      embeddedUrl.type = "unknown";
    }

    return embeddedUrl;
  }

  getEmbedInfoType(embedInfo: EmbedInfo) : string {
    return embedInfo && embedInfo.oembed ? embedInfo.oembed.type : embedInfo.type;
  }

}
