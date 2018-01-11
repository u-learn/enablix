import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Injectable()
export class NavigationService {

  constructor(private router: Router, private route: ActivatedRoute) { }

  goToPortalHome() {
    this.router.navigate(['/portal']);
  }

  goToNewContentEdit(containerQId: string) : void {
    this.router.navigate(['/portal/content/new', containerQId]);
  }

  goToContentDetail(containerQId: string, identity: string, returnUrl: string = "/") {
    this.router.navigate(['/portal/content/detail', containerQId, identity], {queryParams : {returnUrl: returnUrl}});
  }

  goToContentList(containerQId: string, queryParams?: { [key: string] : any }) {
    this.router.navigate(['/portal/content/list', containerQId], { queryParams: queryParams ? queryParams : {}});
  }

  goToDimList(containerQId: string) {
    this.router.navigate(['/portal/dim/list', containerQId]);
  }

  goToDimDetail(containerQId: string, identity: string) {
    this.router.navigate(['/portal/dim/detail', containerQId, identity]);
  }

  goToConsolidateContent() {
    this.router.navigate(['/portal/cconsol']);
  }

  goToFreetextSearch(text: string) {
    this.router.navigate(['/portal/search', text]);
  }

  goToContentRequestDetail(recIdentity: string, returnUrl: string = "/") {
    this.router.navigate(['/portal/content/request', recIdentity], {queryParams: {returnUrl : returnUrl}});
  } 

  goToCompanyPage() {
    this.router.navigate(['/portal/company']);
  }

  goToRoute(routePath: string, routeParams: string[], queryParams?: {[key: string] : string}) {
    
    let navigateParams = [routePath];
    if (routeParams) {
      routeParams.forEach(param => navigateParams.push(param));
    }

    if (queryParams) {
      this.router.navigate(navigateParams, {queryParams: queryParams});  
    } else {
      this.router.navigate(navigateParams);  
    }
    
  }
  
}
