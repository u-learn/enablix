import { Injectable } from '@angular/core';

import { AppContext } from '../../app-context';

const OR_PERM_SEPARATOR = "|";
const AND_PERM_SEPARATOR = "+";

@Injectable()
export class UserService {

  private isLoggedIn = false;
  private user: any;

  constructor() { }

  isUserLoggedIn() {
  	return this.isLoggedIn;
  }

  setUserLoggedIn(user: any) {
  	this.isLoggedIn = true;
    this.user = user;
    this.setAppContextPermissions();
  }

  logoutUser() {
  	this.isLoggedIn = false;
  	this.user = null;
  }

  setAppContextPermissions() {

    if (AppContext.embedded) {
    
      var removePerms: string[] = [
        "VIEW_STUDIO", "MANAGE_CONTENT_REQUEST", "COMPANY_DETAILS", 
        "MANAGE_INTEGRATIONS", "MANAGE_DASHBOARD",
        "VIEW_REPORTS", "BULK_IMPORT", "SUGGEST_CONTENT"
      ];
    
      if (this.user && this.user.authorities) {
             
        var permissions = this.user.authorities;

        var k = permissions.length;
        while (k--) {
          var userPerm = permissions[k].authority;
          if (removePerms.indexOf(userPerm) >= 0) {
            permissions.splice(k, 1);
          }
        }

      }
    }
  }

  getUserDisplayName() {
  	return this.user ? this.user.principal.userProfile.name : null;
  }

  getUsername() {
  	return this.user ? this.user.email : null;
  }

  getUserIdentity() {
    return this.user ? this.user.principal.user.userId : null;
  }

  getUserAccountId() {
    return this.user ? this.user.principal.user.id : null;
  }

  isPasswordSet() : boolean {
    return this.user ? this.user.principal.user.isPasswordSet : false;
  }

  getUserProfile() : boolean {
    return this.user ? this.user.principal.userProfile : null;
  }

  updateUserProfile(userProfile: any) {
    if (this.user) {
      this.user.principal.userProfile = userProfile;
    }
  }

  userHasPermission(checkPerm: string) {
    
    if (this.user) {     
      
      let permCheck: PermissionCheck = new SimplePermissionCheck(checkPerm);
           
      // currently we will only support all OR expression or all AND expression
      if (checkPerm.indexOf(OR_PERM_SEPARATOR) > 0) {
           
        var permArray = checkPerm.split(OR_PERM_SEPARATOR);
        permCheck = new OrPermissionCheck(permArray);
             
      } else if (checkPerm.indexOf(AND_PERM_SEPARATOR) > 0) {
             
        var permArray = checkPerm.split(AND_PERM_SEPARATOR);
        permCheck = new AndPermissionCheck(permArray);
      }
           
      return permCheck.check(this.user);
    }

    return false;
  }

  userHasAllPermissions(permissions: string[]) {
         
    if (permissions) {
      for (var i = 0; i < permissions.length; i++) {
        if (!this.userHasPermission(permissions[i])) {
          return false;
        }
      }
    }
     
    return true;
  }

  userHasAtleastOnePermission(permissions: string[]) {
    if (permissions) {
      for (var i = 0; i < permissions.length; i++) {
        if (this.userHasPermission(permissions[i])) {
          return true;
        }
      }
    }
    return false;
  }

}

interface PermissionCheck {
  check(user: any) : boolean;
}

class SimplePermissionCheck implements PermissionCheck {

  perm: string;

  constructor(perm: string) {
    this.perm = perm;
  }

  check(user: any) : boolean {

    if (!this.perm || this.perm.trim().length == 0) {
      return true;
    }

    if (user && user.authorities) {
             
      var permissions = user.authorities;

      for (var k = 0; k < permissions.length; k++) {
        var userPerm = permissions[k].authority;
        if (userPerm === this.perm) {
          return true;
        }
      }
    }
     
    return false;
  }

}

class OrPermissionCheck {

  permChecks: SimplePermissionCheck[] = [];

  constructor(perms: string[]) {
    perms.forEach(perm => {
      this.permChecks.push(new SimplePermissionCheck(perm));
    });
  }
         
  check(user: any) : boolean {
           
    for (var i = 0; i < this.permChecks.length; i++) {
      if (this.permChecks[i].check(user)) {
        return true;
      }
    }
     
    return false;
  }

}

class AndPermissionCheck {

  permChecks: SimplePermissionCheck[] = [];

  constructor(perms: string[]) {
    perms.forEach(perm => {
      this.permChecks.push(new SimplePermissionCheck(perm));
    });
  }
         
  check(user: any) : boolean {
           
    for (var i = 0; i < this.permChecks.length; i++) {
      if (!this.permChecks[i].check(user)) {
        return false;
      }
    }
     
    return true;
  }
  
}