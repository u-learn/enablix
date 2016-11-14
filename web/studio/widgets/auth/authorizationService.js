enablix.studioApp.factory('AuthorizationService', 
	[
	 			'$state', '$rootScope', '$stateParams', 'StateUpdateService', 'RESTService',
	 	function($state,   $rootScope,   $stateParams,   StateUpdateService,   RESTService) {
	 		
	 		var currentUser = {};		
	 		
	 		var userHasPermission = function(checkPerm) {
	 			
	 			if (enablix.loggedInUser && enablix.loggedInUser.authorities) {
	 				
	 				var permissions = enablix.loggedInUser.authorities;
	 				
	 				for (var k = 0; k < permissions.length; k++) {
	 					
	 					var perm = permissions[k].authority;
	 					if (perm === checkPerm) {
	 						return true;
	 					}
	 				}
	 			}
	 			
	 			return false;
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
			    			"authorization" : "Basic " + btoa(credentials.username + ":" + credentials.password)//,
			    			//"remember-me" : "true",
			    		} : {};
			    		
			    if (credentials && credentials.rememberMe) {
			    	headers["remember-me"] = "true";
			    }
			    
			    RESTService.getForData('user', null, null, function(data) {
				    	
			    		if (data.name) {
				    		
			    			enablix.loggedInUser = data.principal;
				    		
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