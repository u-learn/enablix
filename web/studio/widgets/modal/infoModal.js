enablix.studioApp.factory('InfoModalWindow', 
	[			'$state', '$stateParams', '$modal',
	 	function($state,   $stateParams,   $modal) {
		
			var showInfoWindow = function(title, infoText, size) {
				
				var sz = size || 'sm';
				
				var modalInstance = $modal.open({
				      templateUrl: 'views/modal/infoModal.html',
				      size: sz, // 'sm', 'lg'
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