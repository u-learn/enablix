import { Injectable } from '@angular/core';

@Injectable()
export class UserService {

  private isLoggedIn = false;
  private displayName = "";
  private username = "";

  constructor() { }

  isUserLoggedIn() {
  	return this.isLoggedIn;
  }

  setUserLoggedIn() {
  	this.isLoggedIn = true;
  }

  logoutUser() {
  	this.isLoggedIn = false;
  	this.displayName = "";
  	this.username = "";
  }

  getUserDisplayName() {
  	return this.displayName;
  }

  getUsername() {
  	return this.username;
  }

  setUsername(username: string) {
  	this.username = username;
  }

  setUserDisplayName(displayName: string) {
  	this.displayName = displayName;
  }

}
