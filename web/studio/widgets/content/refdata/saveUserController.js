enablix.studioApp.controller('SaveUserController', [
	        '$scope', '$state', '$stateParams', 'RESTService', 'UserService', '$rootScope', 'Notification','StateUpdateService',
	function($scope,   $state,   $stateParams,   RESTService,   UserService,   $rootScope, Notification,  StateUpdateService) {

		$scope.newUser = {};
		$scope.allRoles = [];
		
		$scope.pageHeading = "Add User";
		if ($state.includes("system.users.edit")) {
			$scope.pageHeading = "Edit User";
		}
		
		var identity = $stateParams.identity;

		UserService.getAllRoles(function(data) {
			
			if (!isNullOrUndefined(data) && data != "") {
				$scope.allRoles = data;
			}
			
			if (!isNullOrUndefined(identity) && identity != "") {
				
				UserService.getUserByIdentity(identity, function(data) {
				
					if (!isNullOrUndefined(data) && data != "") {
					
						$scope.newUser = data.user;
						
						angular.forEach($scope.allRoles, function(role) {
							for (var i = 0; i < data.roles.length; i++) {
								if (role.identity === data.roles[i]) {
									role.selected = true;
									break;
								}
							}
						});
					}
				});
			}
		});
		
		$scope.userRecordEdit=function(identity) {
			 StateUpdateService.goEditUser(identity);
		 }	 
		
		$scope.saveUserData = function() {
			
			var selectedRoles = [];
			
			angular.forEach($scope.allRoles, function(role) {
				if (role.selected) {
					selectedRoles.push(role.identity);
				}
			});
			
			if(selectedRoles.length > 0)
				UserService.addUserData($scope.newUser, selectedRoles);
			else
				Notification.error({message: "Select at least one role", delay: enablix.errorMsgShowTime});
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
