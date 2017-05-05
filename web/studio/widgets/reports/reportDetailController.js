enablix.studioApp.controller('ReportDetailCtrl', 
			['$scope', '$state', '$stateParams', '$rootScope', 'ReportService', 'Notification', 'StateUpdateService', 'UserPreferenceService',
	function( $scope,   $state,   $stateParams,   $rootScope,   ReportService,   Notification,   StateUpdateService, UserPreferenceService) {

		$scope.reportDef = ReportService.getReportDef($stateParams.reportId);
		$scope.data = [];
		$scope.reportFilterValues = {};
		
		var fetchReportData = function(_filterValues) {
			
			// remove search keys with null values as those get treated as null value on back end
			removeNullProperties(_filterValues);
			
			// now run the query
			$scope.reportDef.fetchData(_filterValues, function(data) {
				
				$scope.data = data;
				
				if (data.content) { // check if it is a response with pagination data
					$scope.data = data.content;
				}
				
				if ($scope.reportDef.dataTransformer) {
					$scope.data = $scope.reportDef.dataTransformer($scope.data, $scope.reportFilterValues);
				}
				
			}, function(errorData) {
				Notification.error({message: "Error in retrieving report data", delay: enablix.errorMsgShowTime});
			});
		}
		
		var fetchFilterDefaultValueFromUserPref = function(_filter, _defaultPrefFVs) {
			
			if (!isNullOrUndefined(_defaultPrefFVs)) {
				return _defaultPrefFVs.config[_filter.id];
			}
			
			return null;
		}
		
		if ($scope.reportDef) {
			
			if ($scope.reportDef.init) {
				$scope.reportDef.init($scope);
			}

			$scope.reportDefaultFiltersPrefKey = "report." + $scope.reportDef.id + ".defaultFilterValues";
			var defaultPrefFVs = UserPreferenceService.getPrefByKey($scope.reportDefaultFiltersPrefKey);
			
			var searchFilters = {};
			angular.forEach($scope.reportDef.filters, function(filter) {
				
				// check user preferences first
				var defaultFV = fetchFilterDefaultValueFromUserPref(filter, defaultPrefFVs);
				
				if (isNullOrUndefined(defaultFV) && filter.defaultValue) {
					// check report definition if not present in user pref
					defaultFV = filter.defaultValue();
				}

				if (!isNullOrUndefined(defaultFV)) {
					
					$scope.reportFilterValues[filter.id] = defaultFV;
					
					searchFilters[filter.id] = filter.filterValueTransformer ? 
							filter.filterValueTransformer(defaultFV) : defaultFV;
				}
					
			});
			
			fetchReportData(searchFilters);
			
			$scope.onReportSearch = function(_filterValues) {
				fetchReportData(_filterValues);
			};
			
		}
				
	}
]);