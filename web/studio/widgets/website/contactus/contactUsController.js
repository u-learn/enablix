enablix.studioApp.controller('ContactUsController', 
			['$scope', '$stateParams', 'StateUpdateService', 'Notification', 'RESTService', 'InfoModalWindow',
	function( $scope,   $stateParams,   StateUpdateService,   Notification,   RESTService,   InfoModalWindow) {
		
		$scope.contactUsForm = {
				name: "",
				email: "",
				companyName: "",
				message: ""
			};
				
		RESTService.getForData("captchasitekey", null, null, function(data) {
			enablix.captchaSiteKey = data.sitekey;
			$scope.captchaSiteKey = enablix.captchaSiteKey;
		}, function(resp) {
			console.log(resp);
		});
		
		$scope.captchaResponse = null;
		$scope.noCaptchaControl = {};
		
		$scope.submitContactUs = function() {
			
			if (isNullOrUndefined($scope.captchaResponse)) {
				$scope.captchaError = "CAPTCHA_ERROR";
				return;
			} else {
				$scope.captchaError = null;
			}
			
			var contactUsRequest = {
					contactUs : $scope.contactUsForm,
					captchaResponse : $scope.captchaResponse
			}
			
			RESTService.postForData("contactUs", null, contactUsRequest, null, function() {

				InfoModalWindow.showInfoWindow("Thank you.", "Thank you for contacting us. We will get back to you soon!");
				
			}, function(data) {
				
				if (data.message == 'CAPTCHA_ERROR') {
					$scope.captchaError = "CAPTCHA_ERROR";
					$scope.noCaptchaControl.reset();
				} else {
					InfoModalWindow.showInfoWindow("Error.", "Error in submitting request. Please try again later.");
				}
			});
		}
		
		$scope.captchaExpiredCallback = function() {
			$scope.contactUsForm.nocaptcha = null;
		}
		
	}
]);