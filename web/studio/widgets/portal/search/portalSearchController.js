enablix.studioApp.controller('PortalSearchCtrl', 
		   ['$scope', '$stateParams', 'StudioSetupService', 'SearchService', 'Notification',
    function($scope,   $stateParams,   StudioSetupService,   SearchService,   Notification) {

		var searchText = $stateParams.searchText;
		
		SearchService.getSearchResult(searchText, 
			function(data) {
				console.log(data);
			},
			function() {
				Notification.error({message: "Error fetching search result", delay: enablix.errorMsgShowTime});
			}
		);
	}                                          
]);