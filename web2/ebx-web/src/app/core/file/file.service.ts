import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../api-url.service';

@Injectable()
export class FileService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  uploadFile(fileData: FormData) {
    let apiUrl = this.apiUrlService.postFileUploadUrl();
    return this.http.post(apiUrl, fileData);
  }

}
