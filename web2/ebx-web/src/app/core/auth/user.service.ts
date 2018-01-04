import { Injectable } from '@angular/core';

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
  }

  logoutUser() {
  	this.isLoggedIn = false;
  	this.user = null;
  }

  getUserDisplayName() {
  	return this.user ? this.user.principal.displayName : null;
  }

  getUsername() {
  	return this.user ? this.user.email : null;
  }

  getUserIdentity() {
    return this.user ? this.user.principal.userProfile.identity : null;
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