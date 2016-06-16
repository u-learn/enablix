enablix.studioApp.factory('ModalPageWindow', 
	[			'$modal',
	 	function($modal) {
		
			var showModalPage = function(pageTemplateUrl) {
				var modalInstance = $modal.open({
				      templateUrl: pageTemplateUrl,
				      size: 'lg', // 'sm', 'lg'
				      controller: 'ModalPageController'
				    });
			};

	
			return {
				showModalPage : showModalPage
			};

		
		}
	]);