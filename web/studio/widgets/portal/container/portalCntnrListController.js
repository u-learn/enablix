enablix.studioApp.controller('PortalCntnrListCtrl', 
		   ['$scope', 'ContentTemplateService', '$state', '$stateParams', '$q', 'PubSub', 'ContentDataService', 'StudioSetupService', 'ContentUtil', 'StateUpdateService', 'Notification', 'DataSearchService',
    function($scope,   ContentTemplateService,   $state,   $stateParams,   $q,   PubSub,   ContentDataService,   StudioSetupService,   ContentUtil,   StateUpdateService,   Notification,   DataSearchService) {

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
					field: "modifiedAt",
					direction: "DESC"
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
					$scope.listCountInfo = "(Showing " + (dataPage.totalElements > 0 ? pageOffset + 1 : 0) + " - " 
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
		
		PubSub.subscribe(ContentDataService.contentChangeEventId($stateParams.containerQId), function(data, topic) {
			$scope.pagination.pageNum = 0;
			$scope.fetchData();
		});
		
	}                                          
]);