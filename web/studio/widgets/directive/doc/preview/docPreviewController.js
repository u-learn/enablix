enablix.studioApp.controller('DocPreviewCtrl', 
			['$scope', '$sce', '$modalInstance', 'docMetadata', 'DocPreviewService', 'Notification',
    function ($scope,   $sce,   $modalInstance,   docMetadata,   DocPreviewService,   Notification) {
    
		$scope.previewHtml = "<p>Loading...</p>";
		$scope.docMetadata = docMetadata;
		
		var previewHandler = DocPreviewService.getPreviewHandler(docMetadata);
		
		if (!isNullOrUndefined(previewHandler)) {
			
			$scope.htmlType = previewHandler.htmlType();
			var pHtml = previewHandler.previewHtml(docMetadata);
			
			if ($scope.htmlType == 'html') {
				$scope.previewHtml = $sce.trustAsHtml(pHtml);
			} else if ($scope.htmlType == 'angular-html') {
				$scope.previewHtml = pHtml;
			}
		}
		
		$scope.close = function() {
			$modalInstance.close();
		}
		
	}]);
