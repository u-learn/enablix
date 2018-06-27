import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { UserService } from '../auth/user.service';
import { AuthService } from '../auth/auth.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { TenantService } from '../../services/tenant.service';
import { Permissions } from '../../model/permissions.model';
import { GlobalSearchControllerService } from '../../core/search-bar/global-search-controller.service';

@Component({
  selector: 'ebx-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent implements OnInit {

  @Input() type?: string = 'portal'; 
  username: string;

  tenantLogoExist: boolean = true; 

  constructor(private userService: UserService, private authService: AuthService,
              private navService: NavigationService, public tenantService: TenantService,
              public globalSearchCtrl: GlobalSearchControllerService) { }

  ngOnInit() {
    this.updateUsername();
  	this.authService.loginIn.subscribe(name => {
  		this.updateUsername();
  	});
  }

  private updateUsername() {
  	if (this.userService.isUserLoggedIn()) {
  		this.username = this.userService.getUserDisplayName();
  	} else {
  		this.username = null;
  	}	
  }

  navToHome() {
    this.navService.goToPortalHome();
  }

  navToReports() {
    this.navService.goToReportsHome(); 
  }

  navToCConsol() {
    this.navService.goToContentConsolidate(); 
  }

  navToCompanyPage() {
    if (this.userService.userHasPermission(Permissions.COMPANY_DETAILS)) {
      this.navService.goToCompanyPage();  
    }
  }

}
