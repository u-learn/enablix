enablix.studioApp.controller('ForgotPasswordController', 
			['$scope', '$state', 'RESTService', '$rootScope','Notification', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  $rootScope,Notification,   StateUpdateService) {
		$scope.userData={};
		$scope.resetPassword = function() {
			Notification.primary({message: "Mailing reset instructions...",  delay: 500});			
			RESTService.postForData('resetpassword',null,$scope.userData.userId, null,function(status) {
				if(status){
					Notification.primary({message: "Done. Redirecting...",  delay: 500});
					StateUpdateService.goToLogin();			
					}
				}, function(errorObj) {  		
					});					
		}
		
	}
]);