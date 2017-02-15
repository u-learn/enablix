enablix.studioApp.directive('ebxGenericListBrowser', [
                 'ContentTemplateService', 'ContentDataService', '$modal',
		function (ContentTemplateService,   ContentDataService,   $modal) {

			return {
				restrict : 'E',
				scope : {
					selectionCallback: "=",
					preSelectedRecords: "=?",
					listFilters: "=?",
					filterMetadata: "=?",
					tableHeaders: "=",
					domainType: "=",
					browserHeading: "@",
					projectionFields: "=?",
					labelField: "@"
				},
				link : function(scope, element, attrs) {
						
					scope.openBrowserWindow = function() {
						
						var modalInstance = $modal.open({
						      templateUrl: 'widgets/directive/browser/genericListBrowserWindow.html',
						      size: 'lg', // 'sm', 'lg'
						      controller: 'GenericListBrowserController',
						      backdrop: 'static',
						      resolve: {
						    	  preSelectedRecords: function() { return scope.preSelectedRecords ? scope.preSelectedRecords : []; },
						    	  filterMetadata: function() { return scope.filterMetadata ? scope.filterMetadata : {}; },
						    	  tableHeaders: function() { return scope.tableHeaders; },
						    	  listFilters: function() { return scope.listFilters ? scope.listFilters : {}; },
						    	  domainType: function() { return scope.domainType; },
						    	  browserHeading: function() { return scope.browserHeading ? scope.browserHeading : "Browser"; },
						    	  projectionFields: function() { return scope.projectionFields ? scope.projectionFields : null; },
						    	  labelField: function() { return scope.labelField; }
						      }
						});
						
						modalInstance.result.then(function(selectedRecords) {
							if (scope.selectionCallback) {
								scope.selectionCallback(selectedRecords);
							}
						});
					}
					
				},
				templateUrl : "widgets/directive/browser/genericListBrowser.html"
			};
		} ]);