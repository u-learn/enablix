import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class ConfigInfoService {

  constructor(private http: HttpClient, private urlService: ApiUrlService) { }

  getConfigByKey(key: string) {
    let apiUrl = this.urlService.getConfigInfoUrl(key);
    return this.http.get(apiUrl);
  }

  saveConfiguration(configData: any) {
    let apiUrl = this.urlService.postSaveConfigInfoUrl();
    return this.http.post(apiUrl, configData);
  }

  deleteByIdentity(configIdentity: string) {
    let apiUrl = this.urlService.postDeleteConfigInfoUrl(configIdentity);
    return this.http.post(apiUrl, null);
  }

}
