import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../core/api-url.service';
import { EmbedInfo } from '../model/embed-info.model';
import { EmbeddedUrl } from '../model/embedded-url.model';

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
    
    const embeddedUrl = new EmbeddedUrl();
    
    embeddedUrl.previewImageUrl = this.getThumbnailUrl(embedInfo);
    embeddedUrl.title = embedInfo.title;
    embeddedUrl.url = embedInfo.url;
    embeddedUrl.type = this.getEmbedInfoType(embedInfo);
    
    return embeddedUrl;
  }

  getEmbedInfoType(embedInfo: EmbedInfo) : string {
    return embedInfo.oembed ? embedInfo.oembed.type : embedInfo.type;
  }

}
