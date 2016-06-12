enablix.studioApp.controller('WebsiteMainCtrl', 
		   ['$scope', 'ContactUsModalWindow', 
    function($scope,   ContactUsModalWindow) {

		$scope.showContactUsModal = function() {
			ContactUsModalWindow.showContactUsWindow();
		};
		
	}                                          
]);