enablix.studioApp.factory('ContactUsModalWindow', 
	[			'$state', '$stateParams', 'RESTService', 'Notification', '$modal',
	 	function($state,   $stateParams,   RESTService,   Notification,   $modal) {
		
			var showContactUsWindow = function() {
				var modalInstance = $modal.open({
				      templateUrl: 'views/website/contactUs/contactUs.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'ContactUsController'
				    });
			};

	
			return {
				showContactUsWindow : showContactUsWindow
			};

		
		}
	]);