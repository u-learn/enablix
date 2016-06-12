enablix.studioApp.controller('ContactUsController', 
			['$scope', '$stateParams', 'StateUpdateService', 'Notification', 'RESTService', '$modalInstance', 'InfoModalWindow',
	function( $scope,   $stateParams,   StateUpdateService,   Notification,   RESTService,   $modalInstance,   InfoModalWindow) {
		
		$scope.contactUsForm = {
				name: "",
				email: "",
				companyName: "",
				message: ""
			};
				
		$scope.close = function() {
			$modalInstance.close();
		}
		
		$scope.submitContactUs = function() {
			RESTService.postForData("contactUs", null, $scope.contactUsForm, null, function() {
				$modalInstance.close();
				InfoModalWindow.showInfoWindow("Thank you.", "Thank you for contacting us. We will get back to you soon!");
			}, function() {
				$modalInstance.close();
			});
		}
		
	}
]);