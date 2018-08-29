import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ContentTemplateService } from '../core/content-template.service';

import { AppContext } from '../app-context';

@Injectable()
export class NavigationService {

  constructor(private router: Router, private route: ActivatedRoute,
              private ctService: ContentTemplateService) { }

  goToPortalHome() {
    var homePage = AppContext.homePage || '/portal';
    this.router.navigate([homePage]);
  }

  goToReportsHome() {
    this.router.navigate(['/portal/reports']); 
  }

  goToContentConsolidate() {
    this.router.navigate(['/portal/cconsol']);  
  }

  goToNewContentEdit(containerQId: string) : void {
    this.router.navigate(['/portal/content/new', containerQId]);
  }

  goToContentDetail(containerQId: string, identity: string, atCtxParams: any, returnUrl: string = "/") {
    var queryParams = atCtxParams || {};
    queryParams.returnUrl = returnUrl;
    this.router.navigate(['/portal/content/detail', containerQId, identity], {queryParams : queryParams});
  }

  goToContentList(containerQId: string, queryParams?: { [key: string] : any }) {
    this.router.navigate(['/portal/content/list', containerQId], { queryParams: queryParams ? queryParams : {}});
  }

  goToDimList(containerQId: string) {
    this.router.navigate(['/portal/dim/list', containerQId]);
  }

  goToDimDetail(containerQId: string, identity: string, queryParams: any = {}) {
    this.router.navigate(['/portal/dim/detail', containerQId, identity], 
      { queryParams: queryParams });
  }

  goToRecordDetail(containerQId: string, identity: string, atCtxParams: any, returnUrl: string = "/") {

    let container = this.ctService.getContainerByQId(containerQId);
    
    if (container) {
      
      if (this.ctService.isBusinessContent(container)) {
        this.goToContentDetail(containerQId, identity, atCtxParams, returnUrl);
      }

      if (this.ctService.isBusinessDimension(container)) {
        this.goToDimDetail(containerQId, identity);
      }

    }
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

  goToReport(reportId: string) {
    this.router.navigate(['/portal/reports/d', reportId]);
  }

  goToMyAccount() {
    this.router.navigate(['/portal/myaccount']);
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
