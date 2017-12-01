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

  goToContentDetail(containerQId: string, identity: string) {
    this.router.navigate(['/portal/content/detail', containerQId, identity]);
  }

  goToDimList(containerQId: string) {
    this.router.navigate(['/portal/dim/list', containerQId]);
  }

  goToDimDetail(containerQId: string, identity: string) {
    this.router.navigate(['/portal/dim/detail', containerQId, identity]);
  }

  goToRoute(routePath: string, routeParams: string[]) {
    let navigateParams = [routePath];
    if (routeParams) {
      routeParams.forEach(param => navigateParams.push(param));
    }
    this.router.navigate(navigateParams);
  }
  
}
