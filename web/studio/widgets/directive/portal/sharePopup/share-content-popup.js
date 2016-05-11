enablix.studioApp.factory('shareContentModalWindow', 
	[		'$state', '$stateParams', 'Notification', '$modal',
	 	function( $state,   $stateParams,   Notification,   $modal) {
	
	
		
	var showShareContentModal = function(counter,bodyData,singleHeaders,multiHeaders) {
				var modalInstance = $modal.open({
				      templateUrl: 'widgets/directive/portal/sharePopup/share-content-popup.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'ShareController',
					  resolve: {				    	  
						   bodyData: function() {
				    		  return bodyData;
				    	  },
						   singleHeaders: function() {
				    		  return singleHeaders;
				    	  },
						   multiHeaders: function() {
				    		  return multiHeaders;
				    	  },
						  counter: function() {
				    		  return counter;
				    	  }
					  }
				    });
			};

			return {
				showShareContentModal : showShareContentModal
			};	
			
}]);