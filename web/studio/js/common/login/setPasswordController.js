enablix.studioApp.controller('SetPasswordController', 
			['$scope', '$state', 'RESTService','UserService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  UserService, $rootScope,   StateUpdateService) {
		$scope.passwordData={};
		$scope.setPassword=function(){
			if($scope.passwordData.confirm===$scope.passwordData.password){
				UserService.updatepassword($scope.passwordData.password);
			}
		}
		
	}
]);