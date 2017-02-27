enablix.studioApp.directive('ebxSystemContentBrowser', [
                 'ContentTemplateService', 'ContentDataService', '$modal',
		function (ContentTemplateService,   ContentDataService,   $modal) {

			return {
				restrict : 'E',
				scope : {
					selectionCallback: "=",
					preSelectedRecords: "=?",
					iconClass: "@"
				},
				link : function(scope, element, attrs) {
						
					scope.iconClass = scope.iconClass || "fa fa-plus"
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