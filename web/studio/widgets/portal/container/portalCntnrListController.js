enablix.studioApp.controller('PortalCntnrListCtrl', 
		   ['$scope', 'ContentTemplateService', '$state', '$stateParams', 'ContentDataService', 'StudioSetupService', 'ContentUtil', 'StateUpdateService', 'Notification', 'DataSearchService',
    function($scope,   ContentTemplateService,   $state,   $stateParams,   ContentDataService,   StudioSetupService,   ContentUtil,   StateUpdateService,   Notification,   DataSearchService) {

		$scope.$stateParams = $stateParams;
		
		var containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.containerQId);
		
		 $scope.pagination = {
				pageSize: enablix.defaultPageSize,
				pageNum: $stateParams.page,
				sort: {
					field: "createdAt",
					direction: "ASC"
				}
			};
		
		$scope.listHeaders = ContentUtil.getContentListHeaders(containerDef);
		$scope.dataFilters = {};

		$scope.fetchData = function() {
			
			DataSearchService.getContainerDataSearchResult($stateParams.containerQId, $scope.dataFilters, $scope.pagination, {}, 
				function(dataPage) {
					$scope.listData = dataPage.content;
					$scope.pageData = dataPage;
					angular.forEach($scope.listData, function(item) {
						ContentUtil.decorateData(containerDef, item, true);
					});
				}, 
				function(data) {
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				});
		
		}
		
		$scope.fetchData();
		
		$scope.navToContentDetail = function(contentRecordIdentity) {
			StateUpdateService.goToPortalContainerDetail($stateParams.containerQId, contentRecordIdentity);
		}
		
		$scope.navToItemDetail = function(_containerQId, _contentIdentity) {
			StateUpdateService.goToPortalContainerBody(
					_containerQId, _contentIdentity, 'single', _containerQId);
		}
		
	}                                          
]);