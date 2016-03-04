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
	 		
	 		var userHasPageAccess = function(pagePermission) {
	 			if (!userHasPermission(pagePermission)) {
	 				StateUpdateService.goToAuthError();
	 			}
	 		}
	 		
	 		var authenticate = function(credentials, callback) {

			    var headers = credentials ? 
			    		{authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) } : {};
			    
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
			
			var getCurrentUser = function() {
				return currentUser;
			}
	 		
	 		return {
	 			userHasPermission: userHasPermission,
	 			userHasPageAccess: userHasPageAccess,
	 			authenticate: authenticate,
	 			getCurrentUser: getCurrentUser
	 		};
	 	}
	 ]);