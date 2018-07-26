import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class UiWidgetService {

  constructor(private http: HttpClient, 
    private urlService: ApiUrlService) { }

  getWidgetData(widgetIdentity: string, pageNo: number = 0, pageSize: number = -1) {
    let apiUrl = this.urlService.getUIWidgetData(widgetIdentity, pageNo, pageSize);
    return this.http.get(apiUrl);
  }

}
