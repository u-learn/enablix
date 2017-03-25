enablix.studioApp.controller('SetPasswordController', 
		['$scope', '$state', 'RESTService', '$rootScope', 'StateUpdateService', '$stateParams', 'UserService', 'Notification',
			function( $scope,   $state,   RESTService,   $rootScope,   StateUpdateService, $stateParams, UserService, Notification) {
			// make the body visible only after authentication is complete
			$(document.body).css({"display": "block"});
			var identity = $stateParams.identity;
			
			$scope.passwordData={};
			$scope.setPasswordSubmit = function() {
				if($scope.passwordData.confirm === $scope.passwordData.password){
					UserService.updatepassword( identity, $scope.passwordData.password );
				}
				else{
					Notification.error({message: "Passwords do not match! ", delay: enablix.errorMsgShowTime});
				}
			}
		}
		]);