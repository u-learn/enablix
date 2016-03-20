enablix.studioApp.factory('ManageRecoModalWindow', 
	[			'$state', '$stateParams', 'RESTService', 'Notification', '$modal', 'QuickLinksService',
	 	function($state,   $stateParams,   RESTService,   Notification,   $modal,   QuickLinksService) {
		
			var showAddToRecoWindow = function(contentIdentity) {
				var modalInstance = $modal.open({
				      templateUrl: 'views/content/reco/manageContentReco.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'ManageRecoController',
				      resolve: {
				    	  contentInstanceIdentity: function() {
				    		  return contentIdentity;
				    	  },
				    	  containerQId: function() {
				    		  return $stateParams.containerQId;
				    	  }
				      }
				    });
			};

	
			return {
				showAddToRecoWindow : showAddToRecoWindow
			};

		
		}
	]);