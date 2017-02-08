enablix.studioApp.controller('DocPreviewActionCtrl', 
			['$scope', '$modal', 'DocPreviewService', 'DocService',
    function ($scope,   $modal,   DocPreviewService,   DocService) {
    
		$scope.previewSupported = false;
		$scope.docMetadata = $scope.docMetadata || {};
		
		var checkPreviewSupported = function() {
			var previewHandler = DocPreviewService.getPreviewHandler($scope.docMetadata);
			$scope.previewSupported = !isNullOrUndefined(previewHandler);
		}
		
		if (!$scope.docMetadata.identity) {
			
			DocService.getDocMetadata($scope.docIdentity, function(data) {
				$scope.docMetadata = data;
				checkPreviewSupported();
			});
			
		} else {
			checkPreviewSupported();
		}
		
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
