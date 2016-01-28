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
					StateUpdateService.goToPortalHome();	
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
			
			$rootScope.authenticated = false;
			
			RESTService.postForData('logout', null, null, null, function() {
					// HACK: to clear basic auth associated with browser window, 
					// update it with a bad credentials
					// http://stackoverflow.com/questions/233507/how-to-log-out-user-from-web-site-using-basic-authentication
					authenticate({username: "~~baduser~~", password:"~~"}, function() {
						StateUpdateService.goToLogin();
					});
					
				}, function(data) {
					// HACK: to clear basic auth associated with browser window, 
					// update it with a bad credentials
					authenticate({username: "~~baduser~~", password:"~~"}, function() {
						StateUpdateService.goToLogin();
					});
				}, null, {});
			
		};
		
	}
			
]);