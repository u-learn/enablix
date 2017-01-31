enablix.studioApp.factory('InfoModalWindow', 
	[			'$state', '$stateParams', '$modal',
	 	function($state,   $stateParams,   $modal) {
		
			var showInfoWindow = function(title, infoText) {
				
				var modalInstance = $modal.open({
				      templateUrl: 'views/modal/infoModal.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'InfoModalController',
				      backdrop: 'static',
				      resolve: {
				    	  modalTitle: function() {
				    		  return title;
				    	  },
				    	  modalInfo: function() {
				    		  return infoText;
				    	  }
				      }
				    });
				
				return modalInstance;
				
			};

	
			return {
				showInfoWindow : showInfoWindow
			};

		
		}
	]);