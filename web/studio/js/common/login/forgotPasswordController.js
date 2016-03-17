enablix.studioApp.controller('ForgotPasswordController', 
			['$scope', '$state', 'RESTService','UserService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  UserService, $rootScope,   StateUpdateService) {
		$scope.userData={};
		$scope.resetPassword = function() {
			RESTService.postForData('resetpassword',null,$scope.userData.userId, null,function(status) {
				if(status)
					StateUpdateService.goToLogin();				
				}, function(errorObj) {  		
					});					
		}
		
	}
]);