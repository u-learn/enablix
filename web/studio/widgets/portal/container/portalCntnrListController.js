enablix.studioApp.controller('PortalCntnrListCtrl', 
		   ['$scope', 'ContentTemplateService', '$state', '$stateParams', '$q', 'ContentDataService', 'StudioSetupService', 'ContentUtil', 'StateUpdateService', 'Notification', 'DataSearchService',
    function($scope,   ContentTemplateService,   $state,   $stateParams,   $q,   ContentDataService,   StudioSetupService,   ContentUtil,   StateUpdateService,   Notification,   DataSearchService) {

		$scope.$stateParams = $stateParams;
		
		var containerDef = ContentTemplateService.getConcreteContainerDefinition(
									enablix.template, $stateParams.containerQId);
		
		$scope.containerLabel = containerDef.label;
		$scope.listCountInfo = "";
		$scope.isBizDimension = containerDef.businessCategory === "BUSINESS_DIMENSION";
		
		$scope.dataFilters = {};
		$scope.filtersAvailable = true;
		
		$scope.layout = ($scope.isBizDimension ? "default" : "tile");
		
		var filterMetadata = {};

		$scope.pagination = {
				pageSize: enablix.defaultPageSize,
				pageNum: $stateParams.page,
				sort: {
					field: "createdAt",
					direction: "ASC"
				}
			};
		
		$scope.onFilterSearch = function(_filterValues, _filterMd) {
			
			// remove search keys with null values as those get treated as null value on back end
			removeNullOrEmptyProperties(_filterValues);
			
			$scope.pagination.pageNum = 0;
			
			$scope.dataFilters = _filterValues;
			filterMetadata = _filterMd;
			
			$scope.fetchData();
		}
		
		$scope.listHeaders = ContentUtil.getContentListHeaders(containerDef);
		

		$scope.fetchData = function() {
			
			DataSearchService.getContainerDataSearchResult($stateParams.containerQId, 
				$scope.dataFilters, $scope.pagination, filterMetadata, 
				function(dataPage) {
					$scope.listData = dataPage.content;
					$scope.pageData = dataPage;
					
					var pageOffset = dataPage.number * dataPage.size;
					$scope.listCountInfo = "(Showing " + (pageOffset + 1) + " - " 
						+ (pageOffset + dataPage.numberOfElements) + " of " + dataPage.totalElements + ")";
					
					angular.forEach($scope.listData, function(item) {
						ContentUtil.decorateData(containerDef, item, true);
					});
				}, 
				function(data) {
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				});
		
		}
		
		$scope.$on("cont-filter:filter-init-complete", function(event, _filterDefs) {
			if (_filterDefs.length == 0) {
				$scope.filtersAvailable = false;
			}
		});
		
		
		
		$scope.navToContentDetail = function(contentRecordIdentity) {
			StateUpdateService.goToPortalContainerDetail($stateParams.containerQId, contentRecordIdentity);
		}
		
		$scope.navToItemDetail = function(_containerQId, _contentIdentity) {
			StateUpdateService.goToPortalContainerBody(
					_containerQId, _contentIdentity, 'single', _containerQId);
		}
		
		if ($scope.isBizDimension) {
			$scope.fetchData();
		}
		
	}                                          
]);