enablix.studioApp.directive('ebxSystemContentBrowser', [
                 'ContentTemplateService', 'ContentDataService', '$modal',
		function (ContentTemplateService,   ContentDataService,   $modal) {

			return {
				restrict : 'E',
				scope : {
					selectionCallback: "=",
					preSelectedRecords: "=?"
				},
				link : function(scope, element, attrs) {
						
					scope.openBrowserWindow = function() {
						
						var modalInstance = $modal.open({
						      templateUrl: 'widgets/directive/content/browser/systemBrowserWindow.html',
						      size: 'lg', // 'sm', 'lg'
						      controller: 'SystemContentBrowserController',
						      backdrop: 'static',
						      resolve: {
						    	  preSelectedRecords: function() {
						    		  return scope.preSelectedRecords ? scope.preSelectedRecords : [];
						    	  }
						      }
						});
						
						modalInstance.result.then(function(selectedContentRecords) {
							if (scope.selectionCallback) {
								scope.selectionCallback(selectedContentRecords);
							}
						});
					}
					
				},
				templateUrl : "widgets/directive/content/browser/systemContentBrowser.html"
			};
		} ]);