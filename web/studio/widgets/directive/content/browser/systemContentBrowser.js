enablix.studioApp.directive('ebxSystemContentBrowser', [
                 'ContentTemplateService', 'ContentDataService', '$modal',
		function (ContentTemplateService,   ContentDataService,   $modal) {

			return {
				restrict : 'E',
				scope : {
					selectionCallback: "&"
				},
				link : function(scope, element, attrs) {
						
					scope.openBrowserWindow = function() {
						var modalInstance = $modal.open({
						      templateUrl: 'widgets/directive/content/browser/systemBrowserWindow.html',
						      size: 'lg', // 'sm', 'lg'
						      controller: 'SystemContentBrowserController'
						});
					}
					
				},
				templateUrl : "widgets/directive/content/browser/systemContentBrowser.html"
			};
		} ]);