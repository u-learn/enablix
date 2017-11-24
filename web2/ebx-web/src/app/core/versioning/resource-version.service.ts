import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
 
import { ApiUrlService } from '../api-url.service';
import { ResourceVersion } from './resource-version.model';

@Injectable()
export class ResourceVersionService {

  versions: ResourceVersion[];

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  loadResourceVersions() : Observable<ResourceVersion[]> {
    
    let apiUrl = this.apiUrlService.getAllResourceVersions();
    
    return this.http.get<ResourceVersion[]>(apiUrl)
                .map(
                    data => {
                      localStorage.setItem('resource-versions', JSON.stringify(data));
                      this.versions = data;
                      return data;
                    },
                    error => {
                      // TODO: error handling
                    }
                  );
  }

}


