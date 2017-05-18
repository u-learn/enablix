enablix.studioApp.directive('ebxPreviewDataViewer', function() {

	return {
		restrict: 'E',
		scope : {
			docMetadata : "="		
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PreviewDataViewerCtrl',
		template: "<ebx-image-slider slides='slides'></ebx-image-slider>"
	}
});

enablix.studioApp.controller('PreviewDataViewerCtrl', 
			['$scope', '$timeout', 'DocPreviewService', 'DocService',
	function ($scope,   $timeout,   DocPreviewService,   DocService) {
				
		var docMetadata = $scope.docMetadata;
		
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
