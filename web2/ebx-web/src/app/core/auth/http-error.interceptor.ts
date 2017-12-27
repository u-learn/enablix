import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

    constructor(private router: Router, private route: ActivatedRoute) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // install an error handler
        return next.handle(req).catch((err: HttpErrorResponse) => {

            console.log(err);

            // redirect to login page if not already on login page
            if (err.status == 401 && !this.router.url.startsWith('/login')
                    && !req.url.endsWith('/logout')) {
                this.router.navigate(['login'], { queryParams: {returnUrl: this.router.url} });
                return null;
            }

            if (err.error instanceof Error) {
                // A client-side or network error occurred. Handle it accordingly.
                console.log('An error occurred:', err.error.message);
            } else {
                // The backend returned an unsuccessful response code.
                // The response body may contain clues as to what went wrong,
                console.log(`Backend returned code ${err.status}, body was: ${err.error}`);
            }

            //return Observable.throw(new Error('Error calling server API'));
            return Observable.throw(err);
        });
    }
}