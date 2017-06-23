enablix.studioApp.controller('ReportDetailCtrl', 
			['$scope', '$state', '$stateParams', '$rootScope', 'ReportService', 'Notification', 'StateUpdateService', 'UserPreferenceService',
	function( $scope,   $state,   $stateParams,   $rootScope,   ReportService,   Notification,   StateUpdateService, UserPreferenceService) {

		$scope.reportDef = ReportService.getReportDef($stateParams.reportId);
		$scope.data = [];
		$scope.reportFilterValues = {};
		
		$scope.writeDownloadLink = function(){
			$scope.reportDef.downloadReport();
		};

	    
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
		
		if ($scope.reportDef) {
			
			$scope.reportDefaultFiltersPrefKey = "report." + $scope.reportDef.id + ".defaultFilterValues";
			
			if ($scope.reportDef.init) {
				$scope.reportDef.init($scope);
			}
			
			$scope.onReportSearch = function(_filterValues) {
				fetchReportData(_filterValues);
			};
			
		}
				
	}
]);