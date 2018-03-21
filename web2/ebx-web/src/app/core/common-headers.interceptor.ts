import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';

import { ResourceVersion } from './versioning/resource-version.model';

@Injectable()
export class CommonHeadersInterceptor implements HttpInterceptor {
  
  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // The custom “X-Requested-With” is a conventional header sent by browser clients.
	  // Spring Security responds to it by not sending a “WWW-Authenticate” header in a 
	  // 401 response, and thus the browser will not pop up an authentication dialog 
	  // (which is desirable in our app since we want to control the authentication).
    let headers = req.headers.set('X-Requested-With', 'XMLHttpRequest');

    // add resource version for checking on backend
    let resourceVersionsJSON = localStorage.getItem('resource-versions');
    //console.log("resource version from localstorage: " + resourceVersionsJSON);

    if (resourceVersionsJSON != null) {

      let versions: ResourceVersion[] = JSON.parse(resourceVersionsJSON);
      
      versions.forEach(
          (version) => {
            headers = headers.set("version." + version.resourceName, version.resourceVersion);
          }
        );
    }

    // Clone the request to add the new headers.
    const authReq = req.clone({headers: headers});

    // Pass on the cloned request instead of the original request.
    return next.handle(authReq);
  }
}