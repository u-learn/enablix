import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { UserService } from '../core/auth/user.service';
import { AuthService } from '../core/auth/auth.service';
import { NavigationService } from '../app-routing/navigation.service';
import { TenantService } from '../services/tenant.service';
import { Permissions } from '../model/permissions.model';

@Component({
  selector: 'ebx-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent implements OnInit {

  @Input() type?: string = 'portal'; 
  username: string;

  constructor(private userService: UserService, private authService: AuthService,
              private navService: NavigationService, public tenantService: TenantService) { }

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

  navToCompanyPage() {
    if (this.userService.userHasPermission(Permissions.COMPANY_DETAILS)) {
      this.navService.goToCompanyPage();  
    }
  }

}
