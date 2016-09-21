enablix.studioApp.controller('DocPreviewCtrl', 
			['$scope', '$sce', '$modalInstance', 'docMetadata', 'DocPreviewService',
    function ($scope,   $sce,   $modalInstance,   docMetadata,   DocPreviewService) {
    
		$scope.previewHtml = "<p>Loading...</p>";
		
		var previewHandler = DocPreviewService.getPreviewHandler(docMetadata);
		if (!isNullOrUndefined(previewHandler)) {
			$scope.previewHtml = $sce.trustAsHtml(previewHandler.previewHtml(docMetadata));
		}
		
		$scope.close = function() {
			$modalInstance.close();
		}
				
	}]);
