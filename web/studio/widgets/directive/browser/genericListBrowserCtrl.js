enablix.studioApp.controller('GenericListBrowserController', 
			['$scope', 'DataSearchService', '$modalInstance', 'Notification', 'preSelectedRecords', 'listFilters', 'filterMetadata', 'tableHeaders', 'domainType', 'browserHeading', 'projectionFields', 'labelField',
    function ($scope,   DataSearchService,   $modalInstance,   Notification,   preSelectedRecords,   listFilters,   filterMetadata,   tableHeaders,   domainType,    browserHeading,   projectionFields,  labelField) {

		$scope.selectedContentItems = angular.copy(preSelectedRecords);
		
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		};
		
		$scope.doneBrowsing = function() {
			$modalInstance.close($scope.selectedContentItems);
		};
		
		$scope.addToSelectedContent = function(dataRecord) {
			var indx = $scope.indexInSelectedContent(dataRecord.identity);
			if (indx == -1) {
				$scope.selectedContentItems.push(dataRecord);
			}
		};
		
		$scope.indexInSelectedContent = function(_dataRecordIdentity) {
			for (var i = 0; i < $scope.selectedContentItems.length; i++) {
				if ($scope.selectedContentItems[i].identity == _dataRecordIdentity) {
					return i;
				}
			}
			return -1;
		}
		
		$scope.removeFromSelectedContent = function(dataRecord) {
			$scope.removeFromSelectedContentByIdentity(dataRecord.identity);
		};
		
		$scope.selectedContentItemRemoved = function(_removedContentItem, $index) {
			if ($scope.contentList) {
				angular.forEach($scope.contentList, function(contentItem) {
					if (contentItem.identity == _removedContentItem.identity) {
						contentItem._selected = false;
					}
				});
			}
		}
		
		$scope.removeFromSelectedContentByIdentity = function(_dataIdentity) {
			var indx = $scope.indexInSelectedContent(_dataIdentity);
			if (indx != -1) {
				$scope.selectedContentItems.splice(indx, 1);
			}
		}
		
		$scope.pagination = {
				pageSize: enablix.defaultPageSize,
				pageNum: 0,
				sort: {
					field: "createdAt",
					direction: "DESC"
				}
			};
		
		$scope.browserHeading = browserHeading;
		$scope.labelField = labelField;
		$scope.listFilters = listFilters;
		$scope.tableHeaders = tableHeaders;
		$scope.tableRecordActions = [];
		
		$scope.tableRecordSelectAction = {
			actionCallbackFn: function(record) {
				if (record._selected) {
					$scope.addToSelectedContent(record);
				} else {
					$scope.removeFromSelectedContent(record);
				}
			}
		}
		
		$scope.fetchData = function() {
			
			DataSearchService.getSearchResult(domainType, $scope.listFilters, $scope.pagination, filterMetadata, 
				function(data) {
				
					$scope.contentList = data.content;
					$scope.pageData = data;
					
					// mark selected records
					angular.forEach($scope.contentList, function(contentItem) {
						if ($scope.indexInSelectedContent(contentItem.identity) != -1) {
							contentItem._selected = true; 
						}
					});
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				}, projectionFields);
		}
		
		$scope.fetchData();
		
}]);



