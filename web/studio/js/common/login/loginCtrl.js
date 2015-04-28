enablix.studioApp.controller('LoginController', 
			['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,   $rootScope,   StateUpdateService) {
		
		var authenticate = function(credentials, callback) {

		    var headers = credentials ? 
		    		{authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) } : {};

		    RESTService.getForData('user', null, null, function(data) {
			    	if (data.name) {
			    		$rootScope.authenticated = true;
			    	} else {
			    		$rootScope.authenticated = false;
			    	}
			    	callback && callback();
		    	
		    	}, function() {
		    		$rootScope.authenticated = false;
		    		callback && callback();
		    	}, headers);

		};

		var authCallback = function() {
			if ($rootScope.authenticated) {
				StateUpdateService.goToStudio();
				$scope.error = false;
			} else {
				$scope.error = true;
	        }
		};
		
		authenticate();
		  
		$scope.credentials = {};
		
		$scope.login = function() {
			$rootScope.loginProcess = true;
			authenticate($scope.credentials, function() {
				$rootScope.loginProcess = false;
				authCallback();
		    });
		};
		
		$scope.logout = function() {
			
			RESTService.postForData('logout', null, null, null, function() {
					
					$rootScope.authenticated = false;
					StateUpdateService.goToLogin();
					
				}, function(data) {
					$rootScope.authenticated = false;
					StateUpdateService.goToLogin();
					
				}, null, {});
			
		};
		
	}
			
]);