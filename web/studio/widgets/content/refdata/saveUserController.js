enablix.studioApp.controller('SaveUserController', [
	        '$scope', '$state', '$stateParams', 'RESTService', 'UserService', '$rootScope', 'StateUpdateService',
	function($scope,   $state,   $stateParams,   RESTService,   UserService,   $rootScope,   StateUpdateService) {

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
		
		$scope.saveUserData = function() {
			
			var selectedRoles = [];
			
			angular.forEach($scope.allRoles, function(role) {
				if (role.selected) {
					selectedRoles.push(role.identity);
				}
			});
			
			UserService.addUserData($scope.newUser, selectedRoles);
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