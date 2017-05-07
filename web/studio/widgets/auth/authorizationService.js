enablix.studioApp.factory('AuthorizationService', 
	[
	 			'$state', '$rootScope', '$stateParams', 'StateUpdateService', 'RESTService', 'TenantInfoService',
	 	function($state,   $rootScope,   $stateParams,   StateUpdateService,   RESTService,   TenantInfoService) {
	 		
	 		var currentUser = {};	
	 		
	 		var OR_PERM_SEPARATOR = "|";
	 		var AND_PERM_SEPARATOR = "+";
	 		
	 		function SimplePermissionCheck(perm) {
	 			
	 			this.perm = perm;
	 			
	 			this.check = function() {
	 				
	 				if (enablix.loggedInUser && enablix.loggedInUser.authorities) {
		 				
		 				var permissions = enablix.loggedInUser.authorities;
		 				
		 				for (var k = 0; k < permissions.length; k++) {
		 					
		 					var userPerm = permissions[k].authority;
		 					if (userPerm === this.perm) {
		 						return true;
		 					}
		 				}
		 			}
		 			
		 			return false;
	 			}
	 			
	 		};
	 		
	 		function OrPermissionCheck(_perms) {

	 			this.permChecks = [];
	 				
	 			for (var i = 0; i < _perms.length; i++) {
	 				this.permChecks[i] = new SimplePermissionCheck(_perms[i]);
	 			}
	 			
	 			this.check = function() {
	 				
	 				for (var i = 0; i < this.permChecks.length; i++) {
	 					if (this.permChecks[i].check()) {
	 						return true;
	 					}
	 				}
	 				
	 				return false;
	 			}
	 		}
	 		
	 		function AndPermissionCheck(_perms) {

	 			this.permChecks = [];
	 				
 				for (var i = 0; i < _perms.length; i++) {
 					this.permChecks[i] = new SimplePermissionCheck(_perms[i]);
 				}
	 			
	 			this.check = function() {
	 				
	 				for (var i = 0; i < this.permChecks.length; i++) {
	 					if (!this.permChecks[i].check()) {
	 						return false;
	 					}
	 				}
	 				
	 				return true;
	 			}
	 		}
	 		
	 		var userHasPermission = function(checkPerm) {
	 			
	 			var permCheck = new SimplePermissionCheck(checkPerm);
	 			
	 			// currently we will only support all OR expression or all AND expression
	 			if (checkPerm.indexOf(OR_PERM_SEPARATOR) > 0) {
	 			
	 				var permArray = checkPerm.split(OR_PERM_SEPARATOR);
	 				permCheck = new OrPermissionCheck(permArray);
	 				
	 			} else if (checkPerm.indexOf(AND_PERM_SEPARATOR) > 0) {
	 				
	 				var permArray = checkPerm.split(AND_PERM_SEPARATOR);
	 				permCheck = new AndPermissionCheck(permArray);
	 			}
	 			
	 			return permCheck.check();
	 		}
	 		
	 		var userHasAllPermissions = function(permissions) {
	 			
	 			if (permissions) {
		 			for (var i = 0; i < permissions.length; i++) {
		 				if (!userHasPermission(permissions[i])) {
		 					return false;
		 				}
		 			}
	 			}
	 			
	 			return true;
	 		}
	 		
	 		var userHasPageAccess = function(pagePermission) {
	 			if (!userHasPermission(pagePermission)) {
	 				StateUpdateService.goToAuthError();
	 			}
	 		}
	 		
	 		var authenticate = function(credentials, callback) {

			    var headers = credentials ? 
			    		{ 
			    			"authorization" : "Basic " + btoa(credentials.username.toLowerCase() + ":" + credentials.password)//,
			    			//"remember-me" : "true",
			    		} : {};
			    		
			    if (credentials && credentials.rememberMe) {
			    	headers["remember-me"] = "true";
			    }
			    
			    RESTService.getForData('user', null, null, function(data) {
				    	
			    		if (data.name) {
				    		
			    			enablix.loggedInUser = data.principal;
				    		TenantInfoService.initFromLoggedInUser(enablix.loggedInUser);
			    			
				    		currentUser = data.principal.user;
				    		window.localStorage.setItem("userData", JSON.stringify(data.principal.user));
				    	
				    		$rootScope.authenticated = true;
				    		
				    	} else {
				    		window.localStorage.setItem("userData","");
				    		$rootScope.authenticated = false;
				    	}
			    		
				    	callback && callback();
			    	
			    	}, function() {
			    		
			    		window.localStorage.setItem("userData","");
			    		$rootScope.authenticated = false;
			    		callback && callback();
			    		
			    	}, headers);

			};
			
			var logoutUser = function() {
				
				$rootScope.authenticated = false;
				
				RESTService.postForData('logout', null, null, null, function() {
						// HACK: to clear basic auth associated with browser window, 
						// update it with a bad credentials
						// http://stackoverflow.com/questions/233507/how-to-log-out-user-from-web-site-using-basic-authentication
						authenticate({username: "~~baduser~~", password:"~~"}, function() {
							StateUpdateService.goToWebsite();
						});
						
					}, function(data) {
						// HACK: to clear basic auth associated with browser window, 
						// update it with a bad credentials
						authenticate({username: "~~baduser~~", password:"~~"}, function() {
							StateUpdateService.goToWebsite();
						});
					}, null, {});
			}
			
			var getCurrentUser = function() {
				return currentUser;
			}
	 		
	 		return {
	 			userHasPermission: userHasPermission,
	 			userHasAllPermissions: userHasAllPermissions,
	 			userHasPageAccess: userHasPageAccess,
	 			authenticate: authenticate,
	 			logoutUser: logoutUser,
	 			getCurrentUser: getCurrentUser
	 		};
	 	}
	 ]);