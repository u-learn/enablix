enablix.studioApp.controller('ShareController', [
			 '$scope', '$stateParams', '$modalInstance', '$timeout', 'containerQId', 'contentIdentity', 'ContentShareService', 'Notification', 'RESTService',
	function( $scope,   $stateParams,   $modalInstance,   $timeout,   containerQId,   contentIdentity,   ContentShareService,   Notification,   RESTService) {
		
				 $scope.validationCheck = false;
		var email;
		
		$scope.querySearch = function (query) {
			return query ? $scope.users.filter( createFilterFor(query) ) : $scope.users;
		}
		
		function validateEmail() {
			
			if ($scope.selectedItem == null) {
				
				email = $scope.searchText;
				
				var ltpos = email.indexOf("<");
				var gtpos = email.indexOf(">");
				
				if (ltpos >= 0 && gtpos > 0) {
					email = email.substr(ltpos + 1, gtpos - ltpos - 1);
				}
				
			} else {
				email = $scope.selectedItem.value;
			}
			
			var atpos = email.indexOf("@");
			var dotpos = email.lastIndexOf(".");
			
			if ( atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length ) {
				return false;
			}
			
			return true;
		}
		
		function getAllUsers(_success){
			RESTService.getForData('systemuser', null, null, function(data) {
				_success(data);	    	
			}, function() {    		
				Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
			});
		};
		
		function populateUserIds(data){
			$scope.users = data.map(function (dataItem) {
				return {
					value: dataItem.email.toLowerCase(),
					display: (dataItem.name + " <" + dataItem.email.toLowerCase() + ">")
				};
			});
		}

		function createFilterFor(query) {
			
			var lowercaseQuery = angular.lowercase(query);

			return function filterFn(state) {
				return (state.value.indexOf(lowercaseQuery) === 0);
			};
		}
		
		$scope.shareContent = function() {
			
			var valid = validateEmail();
			if (valid) {
				
				ContentShareService.shareContent(containerQId, contentIdentity, email, $scope.emailContent ,function(sent) {
				
					if(sent) {
						Notification.primary({message: "Mail sent successfully", delay: enablix.errorMsgShowTime});
					} else {
						Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
					}
					
				}, function(fail) {
					Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
				});

				$modalInstance.close();
				
			} else {
				Notification.error({message: "Please enter a valid E-Mail ID ", delay: 3000});
			}
		}

		$scope.close = function() {
			$modalInstance.dismiss('cancel');
		}
		
		getAllUsers(populateUserIds);
		
	}]);
