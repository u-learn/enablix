enablix.studioApp.controller('EditUserController', [
	'$scope', '$state', '$stateParams', 'RESTService', 'UserService', '$rootScope', 'Notification','StateUpdateService','ContentOperInitService',
	function($scope,   $state,   $stateParams,   RESTService,   UserService,   $rootScope, Notification,  StateUpdateService, ContentOperInitService) {

		$scope.user = {};
		$scope.userProfile = {};
		$scope.allRoles = [];

		var containerQId = "user";
		var parentIdentity = $scope.parentIdentity = $stateParams.parentIdentity;


		$scope.pageHeading = "Edit User";
		
		var identity = $stateParams.identity;

		UserService.getAllRoles(function(data) {

			if (!isNullOrUndefined(data) && data != "") {
				$scope.allRoles = data;
			}

			if (!isNullOrUndefined(identity) && identity != "") {

				UserService.getUserByIdentity(identity, function(data) {

					if (!isNullOrUndefined(data) && data != "") {
						$scope.userProfile = data;
						ContentOperInitService.initEditContentOperWithData($scope, containerQId, $scope.userProfile.businessProfile.attributes);
						angular.forEach($scope.allRoles, function(role) {
							
							for (var i = 0; i < data.systemProfile.roles.length; i++) {
								if (role.identity === data.systemProfile.roles[i].identity) {
									role.selected = true;
									break;
								}
							}
						});
					}
				});
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
				
				UserService.editUserData($scope.user,$scope.userProfile);
			}
			else
				Notification.error({message: "Select at least one role", delay: enablix.errorMsgShowTime});
		}
		$scope.userRecordEdit=function() {
			 StateUpdateService.goEditUser(identity);
		 }
		$scope.cancelOperation = function() {
			StateUpdateService.goToListUser();
		};

		$scope.checkUserName = function() {

			if ($scope.newUser) {

				UserService.checkUserName($scope.newUser.userId, 
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
