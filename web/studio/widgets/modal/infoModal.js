enablix.studioApp.factory('InfoModalWindow', 
	[			'$state', '$stateParams', 'RESTService', 'Notification', '$modal',
	 	function($state,   $stateParams,   RESTService,   Notification,   $modal) {
		
			var showInfoWindow = function(title, infoText) {
				var modalInstance = $modal.open({
				      templateUrl: 'views/modal/infoModal.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'InfoModalController',
				      resolve: {
				    	  modalTitle: function() {
				    		  return title;
				    	  },
				    	  modalInfo: function() {
				    		  return infoText;
				    	  }
				      }
				    });
			};

	
			return {
				showInfoWindow : showInfoWindow
			};

		
		}
	]);