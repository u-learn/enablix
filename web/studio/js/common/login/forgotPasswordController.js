enablix.studioApp.controller('ForgotPasswordController', 
			['$scope', '$state', 'RESTService','UserService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  UserService, $rootScope,   StateUpdateService) {
		$scope.userData={};


		$scope.resetPassword = function() {
			/* if($scope.userData.confirm===$scope.passwordData.password){
				UserService.updatepassword($scope.passwordData.password);
			} */
			RESTService.postForData('resetpassword',null,$scope.userData.userId, null,function(status) {
				if(status)
					StateUpdateService.goToLogin();
						/* Notification.primary({message: "Reset mail sent successfully!", delay: enablix.errorMsgShowTime});
				else
						Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime}); */
				
					}, function(errorObj) {    		
						Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
					});	
			
				//UserService.sendMail(null,$scope.userData.userId,"forgotpassword");
			
				
		}
		
	}
]);