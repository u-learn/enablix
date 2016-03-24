enablix.studioApp.controller('ForgotPasswordController', 
			['$scope', '$state', 'RESTService', '$rootScope','Notification', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  $rootScope,Notification,   StateUpdateService) {
		$scope.userData={};
		$scope.resetPassword = function() {
			//Notification.primary({message: "Mailing reset instructions...",  delay: 500});			
			RESTService.postForData('resetpassword',null,$scope.userData.userId, null,function(status) {
				if(status){
					Notification.primary({message: "Please check your mail for instructions. Redirecting...",  delay: 3000});
					StateUpdateService.goToLogin();			
					}
					else {
						Notification.primary({message: "User Not Found! Try again.",  delay: 3000});
					}
				}, function(errorObj) {  	
					});					
		}
		
	}
]);