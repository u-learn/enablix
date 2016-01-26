enablix.studioApp.controller('LoginController', 
			['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,   $rootScope,   StateUpdateService) {
		var currentUser={};
		var authenticate = function(credentials, callback) {

		    var headers = credentials ? 
		    		{authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) } : {};

		    RESTService.getForData('user', null, null, function(data) {
			    	if (data.name) {
			    		currentUser=data.principal.user;
			    		window.localStorage.setItem("userData",JSON.stringify(data.principal.user));
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

		var authCallback = function() {
			if ($rootScope.authenticated) {
				if(currentUser.isPasswordSet){
					StateUpdateService.goToStudio();	
				}else {
					StateUpdateService.goToSetPassword();
				}				
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
					window.localStorage.setItem("userData","");
					$rootScope.authenticated = false;
					StateUpdateService.goToLogin();
					
				}, function(data) {
					$rootScope.authenticated = false;
					StateUpdateService.goToLogin();
					
				}, null, {});
			
		};
		
	}
			
]);