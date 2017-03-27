enablix.studioApp.controller('LoginController', 
			['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService', 'AuthorizationService',
	function( $scope,   $state,   RESTService,   $rootScope,   StateUpdateService, AuthorizationService) {

		$scope.$state = $state;

		var authCallback = function() {
			
			if ($rootScope.authenticated) {
				
				if(AuthorizationService.getCurrentUser().isPasswordSet){
					StateUpdateService.goToPortalHome();	
				} else {
					StateUpdateService.goToSetPassword(AuthorizationService.getCurrentUser().id);
				}	
				
				$scope.error = false;
				
			} else {
				$scope.error = true;
	        }
		};
		
		AuthorizationService.authenticate();
		  
		$scope.credentials = {};
		
		$scope.login = function() {
			$rootScope.loginProcess = true;
			
			AuthorizationService.authenticate($scope.credentials, function() {
				$rootScope.loginProcess = false;
				authCallback();
		    });
		};
		
		$scope.logout = function() {
			AuthorizationService.logoutUser();
		};
		
	}
			
]);