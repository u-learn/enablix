enablix.studioApp.controller('LoginController', 
			['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService', 'AuthorizationService',
	function( $scope,   $state,   RESTService,   $rootScope,   StateUpdateService, AuthorizationService) {

		$scope.$state = $state;

		var authCallback = function() {
			
			if ($rootScope.authenticated) {
				
				if(AuthorizationService.getCurrentUser().isPasswordSet){
					StateUpdateService.goToPortalHome();	
				} else {
					StateUpdateService.goToSetPassword();
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
			
			$rootScope.authenticated = false;
			
			RESTService.postForData('logout', null, null, null, function() {
					// HACK: to clear basic auth associated with browser window, 
					// update it with a bad credentials
					// http://stackoverflow.com/questions/233507/how-to-log-out-user-from-web-site-using-basic-authentication
					AuthorizationService.authenticate({username: "~~baduser~~", password:"~~"}, function() {
						StateUpdateService.goToLogin();
					});
					
				}, function(data) {
					// HACK: to clear basic auth associated with browser window, 
					// update it with a bad credentials
					AuthorizationService.authenticate({username: "~~baduser~~", password:"~~"}, function() {
						StateUpdateService.goToLogin();
					});
				}, null, {});
			
		};
		
	}
			
]);