enablix.studioApp.controller('SetPasswordController', 
			['$scope', '$state', 'RESTService','UserService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  UserService, $rootScope,   StateUpdateService) {
		// make the body visible only after authentication is complete
		$(document.body).css({"display": "block"});
		
		$scope.passwordData={};
		$scope.setPasswordSubmit = function() {
			if($scope.passwordData.confirm===$scope.passwordData.password){
				UserService.updatepassword($scope.passwordData.password);
			}
		}
		
	}
]);