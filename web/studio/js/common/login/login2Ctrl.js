enablix.studioApp.controller('Login2Controller', 
			['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService', 'AuthorizationService',
	function( $scope,   $state,   RESTService,   $rootScope,   StateUpdateService,   AuthorizationService) {

		$scope.$state = $state;
		  
		$scope.credentials = {};
				
		var authCallback = function() {
			
			if ($rootScope.authenticated) {
				
				var currentUser = AuthorizationService.getCurrentUser();
				
				if(currentUser.isPasswordSet){
					StateUpdateService.goToApp();	
				} else {
					StateUpdateService.goToAppSetPassword();
				}
				
				$scope.error = false;
				
			} else {
				$scope.error = true;
	        }
		};
		
		$scope.login = function() {
			$rootScope.loginProcess = true;
			AuthorizationService.authenticate($scope.credentials, function() {
				$rootScope.loginProcess = false;
				authCallback();
		    });
		};

		$scope.forgotpassword = function(){
			StateUpdateService.goToForgotPasswordPage();
		}
		
	}
			
]);