enablix.studioApp.factory('AuthorizationService', 
	[
	 			'$state', '$stateParams', 'StateUpdateService',
	 	function($state,   $stateParams,   StateUpdateService) {
	 		
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
	 		
	 		return {
	 			userHasPermission: userHasPermission,
	 			userHasPageAccess: userHasPageAccess 
	 		};
	 	}
	 ]);