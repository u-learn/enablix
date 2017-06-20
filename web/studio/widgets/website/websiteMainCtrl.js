enablix.studioApp.controller('WebsiteMainCtrl', 
		   ['$scope', 'ContactUsModalWindow', 'RESTService', 'ModalPageWindow',
    function($scope,   ContactUsModalWindow,   RESTService,   ModalPageWindow) {

		RESTService.getForData("captchasitekey", null, null, function(data) {
			enablix.captchaSiteKey = data.sitekey;
			$scope.captchaSiteKey = enablix.captchaSiteKey;
		}, function(resp) {
			console.log(resp);
		});
			   
		$scope.showContactUsModal = function() {
			ContactUsModalWindow.showContactUsWindow();
		};
		
		$scope.showAboutUsModal = function() {
			ModalPageWindow.showModalPage('views/website/aboutus.html');
		}
	}                                          
]);