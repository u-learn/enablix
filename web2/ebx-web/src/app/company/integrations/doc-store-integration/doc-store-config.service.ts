import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../../../core/api-url.service';

@Injectable()
export class DocStoreConfigService {

  static DOCUMENT_STORE_CONFIG_KEY_PREFIX = "docstore.";

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  getDefaultDocstoreConfig() {
    let apiUrl = this.apiUrlService.getDefaultDocstoreConfigUrl();
    return this.http.get(apiUrl);
  }

  saveDocstoreConfig(docstoreConfig: any, dsType: string) {
    
    let docstoreData: any = {
      key: DocStoreConfigService.DOCUMENT_STORE_CONFIG_KEY_PREFIX + dsType,
      config: docstoreConfig
    };

    let apiUrl = this.apiUrlService.postSaveDocstoreConfigUrl();
    return this.http.post(apiUrl, docstoreData);
  }

}
