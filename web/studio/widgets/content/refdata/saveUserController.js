enablix.studioApp.controller('SaveUserController', [
	'$scope', '$state', '$stateParams', 'RESTService', 'UserService', '$rootScope', 'Notification','StateUpdateService','ContentOperInitService',
	function($scope,   $state,   $stateParams,   RESTService,   UserService,   $rootScope, Notification,  StateUpdateService, ContentOperInitService) {

		$scope.user = {};
		$scope.userProfile = {};
		$scope.allRoles = [];
		$scope.userProfile.systemProfile={};
		var containerQId = "user";
		var parentIdentity = $scope.parentIdentity = $stateParams.parentIdentity;

		

		$scope.pageHeading = "Add User";
		ContentOperInitService.initAddContentOper($scope, containerQId, parentIdentity);
		
		UserService.getAllRoles(function(data) {

			if (!isNullOrUndefined(data) && data != "") {
				$scope.allRoles = data;
			}
		});


		$scope.saveUserData = function() {
			$scope.userProfile.businessProfile = $scope.containerData; 
			var selectedRoles = [];

			angular.forEach($scope.allRoles, function(role) {
				if (role.selected) {
					selectedRoles.push(role.identity);
				}
			});

			if(selectedRoles.length > 0){
				$scope.userProfile.systemProfile.systemRoles=selectedRoles;
				
				UserService.addUserData($scope.user,$scope.userProfile);
			}
			else
				Notification.error({message: "Select at least one role", delay: enablix.errorMsgShowTime});
		}

		$scope.cancelOperation = function() {
			StateUpdateService.goToListUser();
		};

		$scope.checkUserName = function() {

			if ($scope.userProfile) {

				UserService.checkUserName($scope.userProfile.email, 
						function(status) {
					if (status) {
						$scope.userForm.userId.$setValidity('unexits', false);
					} else {
						$scope.userForm.userId.$setValidity('unexits', true);
					}
				});
			}
		};
	} ]);
