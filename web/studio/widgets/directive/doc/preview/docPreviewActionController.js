enablix.studioApp.controller('DocPreviewActionCtrl', 
			['$scope', '$modal', 'DocPreviewService', 'DocService',
    function ($scope,   $modal,   DocPreviewService,   DocService) {
    
		$scope.previewSupported = false;
		$scope.docMetadata = {};
		
		DocService.getDocMetadata($scope.docIdentity, function(data) {
			$scope.docMetadata = data;
			var previewHandler = DocPreviewService.getPreviewHandler($scope.docMetadata);
			$scope.previewSupported = !isNullOrUndefined(previewHandler);
		})
		
		$scope.openPreviewWindow = function() {
			var modalInstance = $modal.open({
			      templateUrl: 'widgets/directive/doc/preview/docPreviewWindow.html',
			      //size: 'sm', // 'sm', 'lg'
			      controller: 'DocPreviewCtrl',
			      resolve: {
			    	  docMetadata: function() {
			    		  return $scope.docMetadata;
			    	  }
			      }
			    });
		};
				
	}]);
