enablix.studioApp.factory('AssocQuickLinkModalWindow', 
	[			'$state', '$stateParams', 'RESTService', 'Notification', '$modal', 'QuickLinksService',
	 	function($state,   $stateParams,   RESTService,   Notification,   $modal,   QuickLinksService) {
		
			var showAddQuickLinks = function(contentIdentity) {
				var modalInstance = $modal.open({
				      templateUrl: 'views/content/quicklinks/contentQuickLinkAssociation.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'AssociateQuickLinkController',
				      resolve: {
				    	  quickLinkAssociation: function($stateParams, $q) {
				    		  
				    		  var deferred = $q.defer();
				    		  
				    		  QuickLinksService.getContentQuickLinkAssociation(contentIdentity, 
					    			function(data) {
					    			  deferred.resolve(data);
					    		  	}, function(data) {
					    		  		deferred.reject(data);
					    		  	});
				    		  
				    		  return deferred.promise;
				    	  },
				    	  contentInstanceIdentity: function() {
				    		  return contentIdentity;
				    	  }
				      }
				    });
			};

	
			return {
				showAddQuickLinks : showAddQuickLinks
			};

		
		}
	]);