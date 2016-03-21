enablix.studioApp.controller('ForgotPasswordController', 
			['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  $rootScope,   StateUpdateService) {
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