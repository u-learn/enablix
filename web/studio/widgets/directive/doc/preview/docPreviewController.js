enablix.studioApp.controller('DocPreviewCtrl', 
			['$scope', '$sce', '$modalInstance', 'docMetadata', 'DocPreviewService', 'Notification',
    function ($scope,   $sce,   $modalInstance,   docMetadata,   DocPreviewService,   Notification) {
    
		/*$scope.previewHtml = "<p>Loading...</p>";
		
		var previewHandler = DocPreviewService.getPreviewHandler(docMetadata);
		if (!isNullOrUndefined(previewHandler)) {
			$scope.previewHtml = $sce.trustAsHtml(previewHandler.previewHtml(docMetadata));
		}*/
		
		$scope.close = function() {
			$modalInstance.close();
		}
		
		DocPreviewService.getPreviewData(docMetadata.identity, 
				function(data) {
					
					var images = [];
					
					angular.forEach(data.parts, function(image, indx) {
						images.push({url: "/doc/pdp/" + docMetadata.identity + "/" + indx + "/"});
					});
					
					$scope.slides = images;
					
				}, function(errorData) {
					Notification.error({message: "Error getting preview information", delay: enablix.errorMsgShowTime});
				});
		
		$scope.slides = [];
		
	}]);
