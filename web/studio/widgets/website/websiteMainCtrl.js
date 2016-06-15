enablix.studioApp.controller('WebsiteMainCtrl', 
		   ['$scope', 'ContactUsModalWindow', 'RESTService',
    function($scope,   ContactUsModalWindow,   RESTService) {

		RESTService.getForData("captchasitekey", null, null, function(data) {
			enablix.captchaSiteKey = data.sitekey;
		}, function(resp) {
			console.log(resp);
		});
			   
		$scope.showContactUsModal = function() {
			ContactUsModalWindow.showContactUsWindow();
		};
		
	}                                          
]);