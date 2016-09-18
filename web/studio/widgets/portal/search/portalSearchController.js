enablix.studioApp.controller('PortalSearchCtrl', 
		   ['$scope', '$stateParams', 'StudioSetupService', 'SearchService', 'Notification',
    function($scope,   $stateParams,   StudioSetupService,   SearchService,   Notification) {

		$scope.$stateParams = $stateParams;
		var searchText = $stateParams.searchText;
		
		var pagination = {
			pageNum: $stateParams.page
		};
		
		fetchSearchResult = function() {
			SearchService.getSearchResult(searchText, pagination,
				function(data) {
					console.log(data);
					$scope.searchResult = data.content;
					$scope.totalPages = data.totalPages;
					$scope.pageNum = pagination.pageNum;
				},
				function() {
					Notification.error({message: "Error fetching search result", delay: enablix.errorMsgShowTime});
				}
			);
		};
		
		fetchSearchResult();
		
		$scope.setPage = function(pageNum) {
			pagination.pageNum = pageNum;
			fetchSearchResult();
		};
	}                                          
]);