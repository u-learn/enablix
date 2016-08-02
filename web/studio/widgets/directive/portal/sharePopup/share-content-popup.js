enablix.studioApp.factory('shareContentModalWindow', 
			[	 '$state', '$stateParams', 'Notification', '$modal',
	 	function( $state,   $stateParams,   Notification,   $modal) {
	
	
		
	var showShareContentModal = function(containerQId, contentIdentity) {
				var modalInstance = $modal.open({
				      templateUrl: 'widgets/directive/portal/sharePopup/share-content-popup.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'ShareController',
					  resolve: {				    	  
						  containerQId: function() {
				    		  return containerQId;
				    	  },
				    	  contentIdentity : function() {
				    		  return contentIdentity;
				    	  }
					  }
				});
			};

	return {
		showShareContentModal : showShareContentModal
	};	
			
}]);